package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.team.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "competeTeam")
public class CompeteTeam {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompeteType competeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public CompeteTeam(CompeteType competeType, Team team) {
        this.competeType = competeType;
        this.team = team;
    }
}