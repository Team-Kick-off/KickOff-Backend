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
@Table(name = "competeTeams")
public class CompeteTeam {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compete_teams_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompeteType competeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public CompeteTeam(CompeteType competeType, Match match, Team team) {
        this.competeType = competeType;
        this.match = match;
        this.team = team;
    }
}