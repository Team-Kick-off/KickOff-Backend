package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.common.BaseEntity;
import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStatus status;    // 상태(모집중, 모집 완료, 경기중, 경기 완료)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;            // 경기장

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "compete_id")
//    private CompeteTeam CompeteTeam;              // 경기 주최팀 / 상대팀 중간테이블

    @Builder(toBuilder = true)
    public Match(LocalDate matchDate, LocalTime startTime, LocalTime endTime, Level level, Gender gender, MatchStatus status, Field field, CompeteTeam competeTeam) {
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.level = level;
        this.gender = gender;
        this.status = status;
        this.field = field;
//        this.competeTeam = competeTeam;
    }

    public void updateEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
