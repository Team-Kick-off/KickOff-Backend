package com.example.kickoffbackend.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "users")
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    @Column(length = 50)
    private String nickname;

    @Column(length = 100)
    private String address;

    @Column(length = 10)
    private String birth;

    @Builder
    private User(String email, String password, String name, Sex sex, String nickname, String address, String birth) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.nickname = nickname;
        this.address = address;
        this.birth = birth;
    }
}
