package com.example.kickoffbackend.auth;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.config.SecurityConfig;
import com.example.kickoffbackend.exceptionHandler.ApiExceptionHandler;
import com.example.kickoffbackend.exceptionHandler.GlobalExceptionHandler;
import com.example.kickoffbackend.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class, ApiExceptionHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원가입 컨트롤러 테스트")
    class SignUp {
        final String email = "test@naver.com";
        final String password = "thisispasswo12!";
        final String name = "test";
        final String nickname = "test";
        final String birth = "980219";
        final String sex = "M";

        @DisplayName("형식에 맞지 않는 이메일을 입력시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test1() throws Exception {
            // Given
            String nonValidEmail = "nonValidEmail";
            SignUpRequest request = SignUpRequest.builder()
                    .email(nonValidEmail)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이메일 형식이 맞지 않습니다."))
                    .andDo(print());
        }


        @DisplayName("이름없이 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test2() throws Exception {
            // Given
            String nonValidEmail = "nonValidEmail";
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
//                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("사용자 이름은 필수입니다."))
                    .andDo(print());
        }

        @DisplayName("닉네임 없이 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test3() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
//                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("사용자 닉네임은 필수입니다."))
                    .andDo(print());
        }

        @DisplayName("생일없이 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test4() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
//                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("사용자 생년월일은 필수입니다."))
                    .andDo(print());
        }

        @DisplayName("성별없이 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test5() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
//                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("사용자 성별은 필수입니다."))
                    .andDo(print());
        }

        @DisplayName("잘못된 성별로 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test6() throws Exception {
            // Given
            String invalidSex = "T";
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(invalidSex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("성별은 'M' 또는 'W'만 가능합니다."))
                    .andDo(print());
        }

        @DisplayName("유효하지 않은 패스워드로 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test77() throws Exception {
            // Given

            String invalidPassword = "testpassword";
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(invalidPassword)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("비밀번호 형식이 맞지 않습니다."))
                    .andDo(print());
        }
        @DisplayName("유효하지 않은 패스워드로 회원가입시, 400 예외가 반환된다.")
        @Test
        @WithMockUser
        void test7() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
//                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."))
                    .andDo(print());
        }

        @DisplayName("정상 요청으로 회원가입시, 200 OK가 반환된다.")
        @Test
        @WithMockUser
        void test1000() throws Exception {
            // Given
            SignUpRequest request = SignUpRequest.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickname(nickname)
                    .birth(birth)
                    .sex(sex)
                    .build();

            // Expected
            String requestBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("회원가입 성공"))
                    .andDo(print());
        }
    }
}
