package com.example.kickoffbackend.user;

import com.example.kickoffbackend.auth.request.SignUpRequest;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.user.domain.Sex;
import com.example.kickoffbackend.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void signUp(SignUpRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw new ApiException(ErrorCode.EmailAlreadyRegisteredError);
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .address(request.address())
                .birth(request.birth())
                .name(request.name())
                .nickname(request.nickname())
                .sex(Sex.valueOf(request.sex()))
                .build();

        userRepository.save(user);
    }
}
