package com.kuit.moamoa.join.service;

import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.global.jwt.JWTUtil;
import com.kuit.moamoa.join.dto.EmailVerificationRequest;
import com.kuit.moamoa.join.dto.ResetPasswordRequest;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.join.dto.UserAuthRequest;
import com.kuit.moamoa.join.dto.NicknameRequest;
import com.kuit.moamoa.join.oauth2.dto.UserAuthResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final TemplateEngine templateEngine;
    private static final String senderEmail = "moamoaproj@gmail.com";
    private final EmailVerificationService emailVerificationService;
    private final JavaMailSender javaMailSender;
    private boolean isVerified = false;

    private MimeMessage createMail(String mail) {

        String token = jwtUtil.createMailJwt(mail, Status.INACTIVE);

        // 인증 URL
    String verificationUrl = "https://moamoa.store/check?token=" + token;
//        String verificationUrl = "http://localhost:9000/check?token=" + token;
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


    public void sendEmailForPassword(EmailVerificationRequest request) {

        String email = request.getUserMail();
        User findUser = userRepository.findByEmail(email);
        log.info("여기 이메일 email: {}", email);
        if (findUser == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND,"가입되지 않은 유저입니다.");
        }

        MimeMessage message = createMail(email);
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
                tempUser.setPassword("tempPassword");
                userRepository.save(tempUser);
                log.info("이메일 인증 성공: 사용자 활성화 완료");
                isVerified=true;
            }
        } catch (Exception e) {
            log.error("토큰 검증 실패", e);
        }
        return isVerified;
    }

    public boolean getVerificationStatus() {
        isVerified = emailVerificationService.getVerificationStatus();
        return isVerified;
    }

    public UserAuthResponse joinProcess(UserAuthRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        boolean isExist = userRepository.existsByEmail(email);

        User user = userRepository.findByEmail(email);
        Status status = user.getStatus();

        if(isExist && user.getPassword()!=null){
            throw new DuplicateKeyException("이미 가입된 이메일입니다.");
        } else if(isExist && status.equals(Status.INACTIVE)) {
            throw new GlobalException(ErrorCode.UNVERIFIED_USER, "인증되지 않은 유저 이메일입니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return UserAuthResponse.from(user);
    }

    public void setNickname(NicknameRequest request) {

        String email = request.getEmail();
        String nickname = request.getNickname(); //새로운 닉네임

        boolean isExist = userRepository.existsByEmail(email);
        User user = userRepository.findByEmail(email);


        if(!isExist){
            throw new GlobalException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}
