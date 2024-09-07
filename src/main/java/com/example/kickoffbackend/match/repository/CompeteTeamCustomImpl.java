package com.example.kickoffbackend.match.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.example.kickoffbackend.match.domain.QCompeteTeam.competeTeam;

public class CompeteTeamCustomImpl implements CompeteTeamCustom{

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

}
