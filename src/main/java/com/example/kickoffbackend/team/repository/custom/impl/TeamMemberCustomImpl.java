package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
import com.example.kickoffbackend.team.repository.custom.TeamMemberCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.kickoffbackend.team.domain.QTeam.team;
import static com.example.kickoffbackend.team.domain.QTeamMember.*;
import static com.example.kickoffbackend.user.domain.QUser.user;

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

    @Override
    public List<Team> findTeamByUserId(Long userId) {
        return queryFactory.select(teamMember.team)
                .from(teamMember)
                .join(teamMember.user, user)
                .join(teamMember.team, team)
                .where(teamMember.user.id.eq(userId))
                .fetch();
    }

    @Override
    public TeamMember findTeamMemberByUserId(Long userId) {
        return queryFactory.selectFrom(teamMember)
                .join(teamMember.user, user)
                .where(teamMember.user.id.eq(userId))
                .fetchOne();
    }

}
