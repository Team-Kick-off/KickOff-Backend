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

    @Id @GeneratedValue
    private Long id;

    private Position position;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Builder(toBuilder = true)
    private AwayTeamMember(Position position, String nickname, Match match) {
        this.position = position;
        this.nickname = nickname;
        this.match = match;
    }
}
