package com.example.kickoffbackend.test;

import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public CustomApi<Account> getInfo(
            @RequestBody AccountRequest request
    ) {
        Account account = Account.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();

        if(account.getName().equals("test")){
            throw new ApiException(ErrorCode.INVALID_ACCOUNT_ERROR);
        }

        return CustomApi.OK(account);
    }
}
