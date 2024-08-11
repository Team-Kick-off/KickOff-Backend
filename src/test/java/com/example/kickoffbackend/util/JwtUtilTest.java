package com.example.kickoffbackend.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;
    private String secretKey = "egaewgeawglkjawelgkawenklgeklawnglkaenglkeawngea";

    @BeforeEach
    void init(){
        jwtUtil = new JwtUtil(secretKey);
    }

    @Test
    @DisplayName("이메일과 역할, 만료 시간으로 jwt 를 생성한다.")
    void createJwt(){
        // Given
        String email = "tset@naver.com";
        String role = "USER";
        Long expiredMs = 10000L;

        // When
        String jwt = jwtUtil.createJwt(email, role, expiredMs);

        // Then
        assertThat(jwt).isNotNull();
    }

    @Test
    @DisplayName("jwt 에서 email 을 가져온다.")
    void getEmail(){
        // Given
        String email = "tset@naver.com";
        String role = "USER";
        Long expiredMs = 10000L;
        String jwt = jwtUtil.createJwt(email, role, expiredMs);

        // When
        String getEmail = jwtUtil.getEmail(jwt);

        // Then
        assertThat(getEmail).isEqualTo(email);
    }

    @Test
    @DisplayName("jwt 에서 role 을 가져온다.")
    void getRole(){
        // Given
        String email = "tset@naver.com";
        String role = "USER";
        Long expiredMs = 10000L;
        String jwt = jwtUtil.createJwt(email, role, expiredMs);

        // When
        String result = jwtUtil.getRole(jwt);

        // Then
        assertThat(result).isEqualTo(role);
    }

    @Test
    @DisplayName("만료된 jwt가 만료됐는지 확인한다.")
    void isExpired_true(){
        // Given
        String email = "tset@naver.com";
        String role = "USER";
        Long expiredMs = 10L;
        String jwt = jwtUtil.createJwt(email, role, expiredMs);

        // When
        boolean result = jwtUtil.isExpired(jwt);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("정상 jwt 가 만료됐는지 확인한다.")
    void isExpired_false(){
        // Given
        String email = "tset@naver.com";
        String role = "USER";
        Long expiredMs = 10000L;
        String jwt = jwtUtil.createJwt(email, role, expiredMs);

        // When
        boolean result = jwtUtil.isExpired(jwt);

        // Then
        assertThat(result).isFalse();
    }
}