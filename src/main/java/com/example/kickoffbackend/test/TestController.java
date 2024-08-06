package com.example.kickoffbackend.test;

import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Operation(summary = "계정 정보 조회", description = "이 API는 계정 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomApi.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomApi.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomApi.class)))
    })
    @GetMapping("/info")
    public CustomApi<Account> getInfo(
            @RequestBody(description = "계정 요청 정보", required = true) AccountRequest request
    ) {
        Account account = Account.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();

        if (account.getName().equals("test")) {
            throw new ApiException(ErrorCode.INVALID_ACCOUNT_ERROR);
        }

        if(account.getName().equals("server")){
            Integer.parseInt("server");
        }

        return CustomApi.OK(account);
    }
}
