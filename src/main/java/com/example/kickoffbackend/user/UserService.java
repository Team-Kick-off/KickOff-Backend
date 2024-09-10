package com.example.kickoffbackend.user;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.user.domain.Sex;
import com.example.kickoffbackend.user.domain.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    @Transactional
    public void signUp(SignUpRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw new ApiException(ErrorCode.EMAIL_ALREADY_REGISTERED_ERROR);
        }
        User user = request.toEntity(passwordEncoder);

        userRepository.save(user);
    }

    public void sendEmail(String email) {
        String subject = "회원가입 인증이메일입니다.";
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;

        long count = getEmailRequestCount(email);

        if (count >= 5) {
            throw new RuntimeException("이메일 인증 요청 5번 초과로 24시간 동안 이메일 인증 요청을 할 수 없습니다.");
        }

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>이메일 인증</title>
                <style>
                    .email-container {
                        font-family: Arial, sans-serif;
                        padding: 20px;
                        border: 1px solid #ddd;
                        border-radius: 10px;
                        text-align: center;
                        width: 600px;
                        margin: 0 auto;
                    }
                    .email-header {
                        font-size: 24px;
                        font-weight: bold;
                        color: #4CAF50;
                        margin-bottom: 20px;
                    }
                    .email-body {
                        font-size: 16px;
                        color: #333;
                        margin-bottom: 20px;
                    }
                    .email-code {
                        display: inline-block;
                        padding: 10px 20px;
                        font-size: 20px;
                        font-weight: bold;
                        color: #ffffff;
                        background-color: #4CAF50;
                        border-radius: 5px;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="email-header">인증 번호 확인 후<br>이메일 인증을 완료해 주세요.</div>
                    <div class="email-body">
                        안녕하세요? KickOff입니다.<br>
                        아래 인증번호를 입력하시고 회원가입을 계속 작성해 주세요.
                    </div>
                    <div class="email-code">인증번호: %d</div>
                </div>
            </body>
            </html>
        """.formatted(code);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);

            saveVerificationCode(email, String.valueOf(code));
            incrementEmailRequestCount(email);
        } catch (Exception e){

        }
    }

    public void saveVerificationCode(String email, String code){
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    public void incrementEmailRequestCount(String email){
        String key = "email_request_count" + email;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count != null && count == 1){
            redisTemplate.expire(key, 24, TimeUnit.HOURS);
        }
    }

    public long getEmailRequestCount(String email){
        String key = "email_request_count" + email;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0;
    }


    public String getVerificationCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    public void verificationEmail(String code, String savedCode) {
        if (!code.equals(savedCode)){
            throw new ApiException(ErrorCode.EMAIL_BAD_REQUEST_ERROR);
        }
    }

    public CustomApi checkDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            return CustomApi.ERROR(400, "이미 가입된 이메일입니다.");
        }
        return CustomApi.OK("사용 가능한 이메일입니다.");
    }

    public CustomApi checkDuplicatedNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            return CustomApi.ERROR(400, "이미 사용하고 있는 닉네임입니다.");
        }
        return CustomApi.OK("사용 가능한 닉네임입니다..");
    }
}
