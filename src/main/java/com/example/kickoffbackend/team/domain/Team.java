package com.example.kickoffbackend.team.domain;

import com.example.kickoffbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "teamName", "introduction", "rule"})
@Table(name = "teams")
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String teamName;

    private String introduction;

    private String rule;

    private int fileAttached;

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TeamImage> teamImages = new ArrayList<>();

    @Builder
    public Team(String teamName, String introduction, String rule, int fileAttached){
        this.teamName = teamName;
        this.introduction = introduction;
        this.rule = rule;
        this.fileAttached = fileAttached;
    }
}
