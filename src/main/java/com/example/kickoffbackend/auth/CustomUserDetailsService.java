package com.example.kickoffbackend.auth;

import com.example.kickoffbackend.user.UserRepository;
import com.example.kickoffbackend.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElse(null);

        if(user != null){
            return new UserPrincipal(user.getEmail(), user.getPassword());
        }
        return null;
    }
}
