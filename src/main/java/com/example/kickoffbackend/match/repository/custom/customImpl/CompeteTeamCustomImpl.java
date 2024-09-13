package com.example.kickoffbackend.match.repository.custom.customImpl;

import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.match.repository.custom.CompeteTeamCustom;
import com.example.kickoffbackend.team.domain.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

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
    public Team findHomeTeamByMatchId(Long matchId) {
         Team homeTeam = queryFactory.select(competeTeam.team)
                .from(competeTeam)
                .join(competeTeam.match, match)
                .join(competeTeam.team, team)
                .where(competeTeam.match.id.eq(matchId).and(competeTeam.competeType.eq(CompeteType.HomeTeam)))
                .fetchOne();

        if (homeTeam == null) {
            throw new ApiException(ErrorCode.TEAM_NOT_FOUND);
        }

        return homeTeam;
    }

    @Override
    public Team findAwayTeamByMatchId(Long matchId) {
        return queryFactory.select(competeTeam.team)
                .from(competeTeam)
                .join(competeTeam.match, match)
                .join(competeTeam.team, team)
                .where(competeTeam.match.id.eq(matchId).and(competeTeam.competeType.eq(CompeteType.AwayTeam)))
                .fetchOne();
    }
}
