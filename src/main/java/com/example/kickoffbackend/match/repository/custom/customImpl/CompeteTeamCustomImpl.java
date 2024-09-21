package com.example.kickoffbackend.match.repository.custom.customImpl;

import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.match.repository.custom.CompeteTeamCustom;
import com.example.kickoffbackend.team.domain.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.example.kickoffbackend.match.domain.QCompeteTeam.competeTeam;
import static com.example.kickoffbackend.match.domain.QMatch.match;
import static com.example.kickoffbackend.team.domain.QTeam.team;

public class CompeteTeamCustomImpl implements CompeteTeamCustom {

    JPAQueryFactory queryFactory;

    public CompeteTeamCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long countByTeamName(String teamName) {
        return queryFactory.select(competeTeam.count())
                .from(competeTeam)
                .where(competeTeam.team.teamName.eq(teamName))
                .fetchOne();
    }

    @Override
    public Optional<Team> findHomeTeamByMatchId(Long matchId) {
         Team homeTeam = queryFactory.select(competeTeam.team)
                .from(competeTeam)
                .join(competeTeam.match, match)
                .join(competeTeam.team, team)
                .where(competeTeam.match.id.eq(matchId).and(competeTeam.competeType.eq(CompeteType.HOME_TEAM)))
                .fetchOne();

        if (homeTeam == null) {
            throw new ApiException(ErrorCode.TEAM_NOT_FOUND);
        }

        return Optional.ofNullable(homeTeam);
    }

    @Override
    public Optional<Team> findAwayTeamByMatchId(Long matchId) {
        Team awayTeam = queryFactory.select(competeTeam.team)
                .from(competeTeam)
                .join(competeTeam.match, match)
                .join(competeTeam.team, team)
                .where(competeTeam.match.id.eq(matchId).and(competeTeam.competeType.eq(CompeteType.AWAY_TEAM)))
                .fetchOne();

        if (awayTeam == null) {
            throw new ApiException(ErrorCode.TEAM_NOT_FOUND);
        }

        return Optional.ofNullable(awayTeam);
    }

    @Override
    public List<Match> findMatchByTeamId(Long teamId) {
        return queryFactory.select(competeTeam.match)
                .from(competeTeam)
                .join(competeTeam.match, match)
                .join(competeTeam.team, team)
                .where(competeTeam.team.id.eq(teamId))
                .fetch();
    }
}
