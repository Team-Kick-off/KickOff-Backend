package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.domain.QTeam;
import com.example.kickoffbackend.team.domain.QTeamImage;
import com.example.kickoffbackend.team.domain.QTeamMember;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.repository.custom.TeamCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.example.kickoffbackend.team.domain.QTeam.*;
import static com.example.kickoffbackend.team.domain.QTeamImage.teamImage;
import static com.example.kickoffbackend.team.domain.QTeamMember.teamMember;

public class TeamCustomImpl implements TeamCustom {

    JPAQueryFactory queryFactory;

    public TeamCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Optional<Team> findByName(String teamName) {
        Team result = queryFactory
                .selectFrom(team)
                .leftJoin(team.teamImages, teamImage).fetchJoin()
                .where(team.teamName.eq(teamName))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
