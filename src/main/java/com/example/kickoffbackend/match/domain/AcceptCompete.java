package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.common.BaseEntity;
import com.example.kickoffbackend.match.domain.type.AcceptStatus;
import com.example.kickoffbackend.team.domain.TeamMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "acceptCompetes")
public class AcceptCompete extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accept_competes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acceptTeamMember_id")
    private TeamMember acceptTeamMember;

    @Enumerated(EnumType.STRING)
    private AcceptStatus status;

    @Builder
    public AcceptCompete(Match match, TeamMember acceptTeamMember, AcceptStatus status) {
        this.match = match;
        this.acceptTeamMember = acceptTeamMember;
        this.status = status;
    }
}
