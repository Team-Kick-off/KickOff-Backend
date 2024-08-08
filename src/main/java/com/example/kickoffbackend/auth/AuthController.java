package com.example.kickoffbackend.auth;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.CustomApi;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/join")
    public CustomApi<Object> signUp(
            @RequestBody @Valid SignUpRequest request
    ){

        return CustomApi.OK("회원가입 성공");
    }
}
