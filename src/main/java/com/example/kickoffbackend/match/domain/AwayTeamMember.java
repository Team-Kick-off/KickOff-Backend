package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.team.domain.Position;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "awayTeamMembers")
public class AwayTeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "away_team_members_id")
    private Long id;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Builder(toBuilder = true)
    public AwayTeamMember(String nickname, Match match) {
        this.nickname = nickname;
        this.match = match;
    }
}
