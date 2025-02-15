package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.jwt.JWTUtil;
import com.kuit.moamoa.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

//    private static final Logger log = LoggerFactory.getLogger(EmailVerificationService.class);
    private final JavaMailSender javaMailSender;
    private final JWTUtil jwtUtil;
    private static final String senderEmail = "moamoaproj@gmail.com";
    private static int number;
    private final UserRepository userRepository;

    public static void createNumber(){
        number = (int)(Math.random() * (9000)) + 100000;
    }
    public MimeMessage CreateMail(String mail){
        createNumber();
        String token = jwtUtil.createMailJwt(mail, number, Status.INACTIVE);
        log.info("인증번호:{}", number);

        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 링크입니다." + "</h3>";
            body += "<h1>" +"http://localhost:9000/verify-email/check?token=" + token + "</h1>"; //TODO: 배포 uri로 바꾸기
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int sendMail(String mail) {
        User tempUser = User.builder()
                .email(mail)
                .status(Status.INACTIVE)
                .build();
        userRepository.save(tempUser);
        log.info("user:{}", tempUser);

        MimeMessage message = CreateMail(mail);
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
                throw new UsernameNotFoundException("해당 닉네임의 사용자를 찾을 수 없습니다.");
            }

            // 저장된 인증번호와 비교
            if (receivedNumber == number) {
//                tempUser.setStatus(Status.ACTIVE);
//                userRepository.save(tempUser);
                log.info("이메일 인증 성공, 사용자 활성화 완료");
                return true; //TODO: 인증 완료 후 나오는 페이지 설정
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
