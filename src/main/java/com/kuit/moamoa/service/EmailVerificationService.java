package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.exception.GlobalException;
import com.kuit.moamoa.jwt.JWTUtil;
import com.kuit.moamoa.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender javaMailSender;
    private final JWTUtil jwtUtil;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;
    private static final String senderEmail = "moamoaproj@gmail.com";
    private static int number; //TODO: db저장

    public static void createNumber(){
        number = (int)(Math.random() * (9000)) + 100000;
    }

public MimeMessage createMail(String mail) {
    createNumber(); // 인증번호 생성
    String token = jwtUtil.createMailJwt(mail, number, Status.INACTIVE);
    log.info("인증번호: {}", number);

    // 인증 URL
    String verificationUrl = "http://localhost:9000/verify-email/check?token=" + token; //TODO: 배포 uri로 바꾸기

    // Thymeleaf를 이용해 이메일 템플릿을 렌더링
    Context context = new Context();
    context.setVariable("verificationUrl", verificationUrl);

    String emailContent = templateEngine.process("email", context);

    try {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(mail);
        helper.setSubject("모아모아 - 이메일 인증");
        helper.setText(emailContent, true);

        return message;
    } catch (MessagingException e) {
        log.error("이메일 생성 중 오류 발생", e);
        throw new RuntimeException("이메일 생성 실패");
    }
}

    public int sendMail(String userMail) {
        User tempUser = User.builder()
                .email(userMail)
                .status(Status.INACTIVE)
                .build();
        userRepository.save(tempUser);
        log.info("user:{}", tempUser);

        MimeMessage message = createMail(userMail);
        javaMailSender.send(message);

        return number;
    }

    public boolean checkMail(String token) {
        try {
            Claims claims = jwtUtil.parseClaims(token); // 토큰 검증 및 파싱
            String email = claims.get("email", String.class);
            int receivedNumber = claims.get("number", Integer.class);

            log.info("토큰에서 추출한 이메일: {}, 인증번호: {}", email, receivedNumber);

            // 해당 이메일의 User 찾기
            User tempUser = userRepository.findByEmail(email);
            if (tempUser == null) {
                throw new GlobalException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
            }

            // 저장된 인증번호와 비교
            if (receivedNumber == number) {
                tempUser.setStatus(Status.ACTIVE);
                userRepository.save(tempUser);
                log.info("이메일 인증 성공: 사용자 활성화 완료");
                return true;
            } else {
                log.warn("이메일 인증 실패: 인증번호 불일치");
                return false;
            }
        } catch (Exception e) {
            log.error("토큰 검증 실패", e);
            return false;
        }
    }

}
