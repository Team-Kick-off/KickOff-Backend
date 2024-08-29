package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.domain.QTeamMember;
import com.example.kickoffbackend.team.repository.custom.TeamMemberCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.example.kickoffbackend.team.domain.QTeamMember.*;
import static com.querydsl.core.types.ExpressionUtils.count;

public class TeamMemberCustomImpl implements TeamMemberCustom {

    JPAQueryFactory queryFactory;

    public TeamMemberCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long countByUserId(Long userId){
        return queryFactory.select(teamMember.count())
                .from(teamMember)
                .where(teamMember.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public boolean existsByUserIdAndTeam(Long userId, String teamName) {
        return queryFactory
                .select().from(teamMember)
                .where(teamMember.user.id.eq(userId).and(teamMember.team.teamName.eq(teamName)))
                .fetchCount() > 0;
    }
}
