package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.request.EmailVerificationRequest;
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


public MimeMessage createMail(String mail) {

    String token = jwtUtil.createMailJwt(mail, Status.INACTIVE);

    // 인증 URL
    String verificationUrl = "https://moamoa.store/verify-email/check?token=" + token; //TODO: 배포 uri로 바꾸기

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

    public void sendMail(EmailVerificationRequest request) {
        String userMail = request.getUserMail();
        User tempUser = User.builder()
                .email(userMail)
                .status(Status.INACTIVE)
                .build();
        userRepository.save(tempUser);
        log.info("user:{}", tempUser);

        MimeMessage message = createMail(userMail);
        javaMailSender.send(message);
    }

    public boolean checkMail(String token) {
        try {
            Claims claims = jwtUtil.parseClaims(token); // 토큰 검증 및 파싱
            String email = claims.get("email", String.class);
            log.info("email:{}", email);

            // 해당 이메일의 User 찾기
            User tempUser = userRepository.findByEmail(email);
            if (tempUser == null) {
                throw new GlobalException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
            } else{
                tempUser.setStatus(Status.ACTIVE);
                userRepository.save(tempUser);
                log.info("이메일 인증 성공: 사용자 활성화 완료");
                return true;
            }
        } catch (Exception e) {
            log.error("토큰 검증 실패", e);
            return false;
        }
    }

}
