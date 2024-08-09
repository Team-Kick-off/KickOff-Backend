package com.example.kickoffbackend.user;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.user.domain.Sex;
import com.example.kickoffbackend.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "JWT_SECRET=lalkwfmawlifawnfoiawnfioawfnafkslgnaw")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("회원가입 서비스 테스트")
    @Nested
    class signUp{

        @Test
        @DisplayName("이미 존재하는 이메일로 회원가입시 400 예외를 발생시킨다.")
        void test1(){
            // Given
            User user = User.builder()
                    .email("test@naver.com")
                    .password("thisispassword!@")
                    .name("test")
                    .sex(Sex.M)
                    .birth("980219")
                    .nickname("test")
                    .build();

            userRepository.save(user);

            SignUpRequest request = SignUpRequest.builder()
                    .email("test@naver.com")
                    .password("thisispassword!@")
                    .name("test")
                    .sex("M")
                    .birth("980219")
                    .nickname("test")
                    .build();

            // Expected
            assertThatThrownBy(() -> {
                userService.signUp(request);
            })
                    .isInstanceOf(ApiException.class)
                    .hasMessage("이미 가입된 이메일입니다.");
        }

        @Test
        @DisplayName("회원가입 요청을 받아 비밀번호는 암호화하고 유저를 생성하여 데이터베이스에 저장한다.")
        void test1000(){
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email("test@naver.com")
                    .password("thisispassword!@")
                    .name("test")
                    .sex("M")
                    .birth("980219")
                    .nickname("test")
                    .build();

            // When
            userService.signUp(request);

            // Then
            List<User> result = userRepository.findAll();
            assertThat(result).hasSize(1)
                    .extracting("email", "name", "sex", "birth", "nickname")
                    .containsExactlyInAnyOrder(
                            tuple("test@naver.com", "test", Sex.M, "980219", "test")
                    );

            String encodedPassword = result.get(0).getPassword();

            boolean isMatch = bCryptPasswordEncoder.matches("thisispassword!@", encodedPassword);
            assertThat(isMatch).isTrue();
        }
    }
}