package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.match.domain.type.CompeteType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "competeTeamMembers")
public class CompeteTeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compete_team_members_id")
    private Long id;

    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompeteType competeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Builder(toBuilder = true)
    public CompeteTeamMember(String nickname, CompeteType competeType, Match match) {
        this.competeType = competeType;
        this.nickname = nickname;
        this.match = match;
    }
}
