package com.example.kickoffbackend.user.domain;

import com.example.kickoffbackend.team.domain.TeamMember;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "primary_position", joinColumns = @JoinColumn(name = "user_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<String> primaryPositions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder
    private User(String email, String password, String name, Sex sex, String nickname, String address, String birth, List<String> primaryPositions) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.nickname = nickname;
        this.address = address;
        this.birth = birth;
        this.primaryPositions.addAll(primaryPositions);
    }
}
