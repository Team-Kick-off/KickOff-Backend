package com.example.kickoffbackend.auth.request;

import com.example.kickoffbackend.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record SignUpRequest(
        @NotBlank(message = "이메일 필수입니다.") @Email(message = "이메일 형식이 맞지 않습니다.")
        String email,

        /**
         * 비밀번호는 최소 8자 이상이어야 합니다.
         * 비밀번호에는 적어도 하나 이상의 알파벳 대문자 또는 소문자가 포함되어야 합니다.
         * 비밀번호에는 적어도 하나 이상의 숫자가 포함되어야 합니다.
         * 비밀번호에는 적어도 하나 이상의 특수 문자(@$!%*#?&)가 포함되어야 합니다.
         */
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호 형식이 맞지 않습니다.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "사용자 이름은 필수입니다.")
        String name,

        @NotNull(message = "사용자 성별은 필수입니다.")
        @Pattern(regexp = "^[MW]$", message = "성별은 'M' 또는 'W'만 가능합니다.")
        String sex,

        @NotBlank(message = "사용자 닉네임은 필수입니다.")
        String nickname,

        @NotBlank(message = "사용자 생년월일은 필수입니다.")
        String birth,

        String address
) {

}
