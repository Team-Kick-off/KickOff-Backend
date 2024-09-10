package com.example.kickoffbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KickOffBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KickOffBackendApplication.class, args);
    }

}
