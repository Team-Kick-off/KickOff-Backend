package com.example.kickoffbackend.team.domain;

import com.example.kickoffbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "introduction", "rule"})
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

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teams = new ArrayList<>();
}
