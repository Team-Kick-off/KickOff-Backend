package com.example.kickoffbackend.auth;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    @PostMapping("/join")
    public CustomApi<Object> signUp(
            @RequestBody @Valid SignUpRequest request
    ){
        userService.signUp(request);
        return CustomApi.OK("회원가입 성공");
    }
}
