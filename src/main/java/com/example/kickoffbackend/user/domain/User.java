package com.example.kickoffbackend.user.domain;

import jakarta.persistence.*;

@Table(name = "users")
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
}
