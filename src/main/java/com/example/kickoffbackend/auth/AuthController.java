package com.example.kickoffbackend.auth;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.user.UserService;
import com.example.kickoffbackend.user.dto.EmailCheckDto;
import com.example.kickoffbackend.user.dto.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    @PostMapping("auth/join")
    public CustomApi<Object> signUp(
            @RequestBody @Valid SignUpRequest request
    ){
        userService.signUp(request);
        return CustomApi.OK("회원가입 성공");
    }


    @PostMapping("/email")
    public CustomApi checkEmail(
            @RequestBody EmailRequestDto emailRequestDto
    ){
        try {
            userService.sendEmail(emailRequestDto.getEmail());
            return CustomApi.OK("이메일 인증 성공");
        } catch (Exception e) {
            return CustomApi.ERROR(new ApiException(ErrorCode.EMAIL_BAD_REQUEST_ERROR));
        }
    }

    @PostMapping("/email/verify")
    public CustomApi verificationEmail(@RequestBody EmailCheckDto emailCheckDto){
        String email = emailCheckDto.getEmail();
        String code = emailCheckDto.getCode();
        String savedCode = userService.getVerificationCode(email);
        userService.verificationEmail(code, savedCode);
        return CustomApi.OK("인증 성공");
    }

    @GetMapping("/auth/email/{email}")
    public CustomApi checkDuplicatedEmail(
            @PathVariable(name = "email") String email
    ) {
        return userService.checkDuplicatedEmail(email);
    }

    @GetMapping("/auth/nickname/{nickname}")
    public CustomApi checkDuplicatedNickname(
            @PathVariable(name = "nickname") String nickname
    ){
        return userService.checkDuplicatedNickname(nickname);
    }
}
