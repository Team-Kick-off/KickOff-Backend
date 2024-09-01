package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.common.BaseEntity;
import com.example.kickoffbackend.match.domain.type.Level;
import com.example.kickoffbackend.match.domain.type.MatchStatus;
import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matchs")
public class Match extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate matchDate;

    @Column(nullable = false)
    private LocalTime startTime;

    private LocalTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(nullable = false)
    private String fieldName;

    private String fieldAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competeTeam_id")
    private CompeteTeam competeTeam; // 경기 주최팀 / 상대팀 중간테이블

    @OneToMany(mappedBy = "match")
    private List<AcceptCompete> acceptCompete;

    @OneToMany(mappedBy = "match")
    private List<HomeTeamMember> homeTeamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "match")
    private List<AwayTeamMember> awayTeamMembers = new ArrayList<>();

    @Builder(toBuilder = true)
    public Match(LocalDate matchDate, LocalTime startTime, LocalTime endTime, Level level, Gender gender, MatchStatus status, String fieldName, String fieldAddress, CompeteTeam competeTeam) {
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.level = level;
        this.gender = gender;
        this.status = MatchStatus.RECRUITING;
        this.fieldName = fieldName;
        this.fieldAddress = fieldAddress;
        this.competeTeam = competeTeam;
    }

    public void updateEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
