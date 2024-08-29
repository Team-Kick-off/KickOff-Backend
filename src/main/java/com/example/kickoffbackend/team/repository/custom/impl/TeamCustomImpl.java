package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.domain.*;
import com.example.kickoffbackend.team.dto.request.TeamFilterRequest;
import com.example.kickoffbackend.team.repository.custom.TeamCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.example.kickoffbackend.team.domain.QTeam.*;
import static com.example.kickoffbackend.team.domain.QTeamImage.teamImage;
import static com.example.kickoffbackend.team.domain.QTeamMember.teamMember;
import static org.springframework.util.StringUtils.isEmpty;

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
                .leftJoin(team.teamMembers, teamMember)
                .where(team.teamName.eq(teamName))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Team> findByTeamFilter(TeamFilterRequest teamFilterRequest) {
        return queryFactory.selectFrom(team)
                .where(
                        addressEq(teamFilterRequest.getAddress()),
                        genderEq(teamFilterRequest.getGender()),
                        statusEq(teamFilterRequest.getStatus())
                )
                .where()
                .fetch();
    }

    private BooleanExpression addressEq(String address) {
        return isEmpty(address) ? null : team.address.eq(address);
    }

    private BooleanExpression genderEq(Gender gender) {
        return isEmpty(gender) ? null : team.gender.eq(gender);
    }

    private BooleanExpression statusEq(RecruitmentStatus status) {
        return isEmpty(status) ? null : team.status.eq(status);
    }
}
