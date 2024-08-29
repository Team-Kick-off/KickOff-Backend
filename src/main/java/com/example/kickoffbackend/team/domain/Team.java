package com.example.kickoffbackend.team.domain;

import com.example.kickoffbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "teamName", "teamIntroduction", "teamRule"})
@Table(name = "teams")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String teamName;

    private String teamIntroduction;

    private String teamRule;

    private Gender gender;

    private String address;

    private String teamLevel;

    private int fileAttached;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TeamImage> teamImages = new ArrayList<>();

    @Builder
    public Team(String teamName, String teamIntroduction, String teamRule, int fileAttached, String address, Gender gender, String teamLevel){
        this.teamName = teamName;
        this.address = address;
        this.teamLevel = teamLevel;
        this.gender = gender;
        this.teamIntroduction = teamIntroduction;
        this.teamRule = teamRule;
        this.fileAttached = fileAttached;
    }
}
