package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.domain.*;
import com.example.kickoffbackend.team.dto.request.TeamFilterRequest;
import com.example.kickoffbackend.team.repository.custom.TeamCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.kickoffbackend.team.domain.QTeam.*;
import static com.example.kickoffbackend.team.domain.QTeamImage.teamImage;
import static com.example.kickoffbackend.team.domain.QTeamMember.teamMember;
import static com.example.kickoffbackend.team.domain.RecruitmentStatus.*;
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
    public List<Team> findByTeamFilter(String address, Gender gender, RecruitmentStatus status, Pageable pageable) {

        BooleanExpression expression = statusEq(status)
                .and(addressEq(address))
                .and(genderEq(gender));


        return queryFactory.selectFrom(team)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public long countByTeamFilter(String address, Gender gender, RecruitmentStatus status) {
        BooleanExpression expression = statusEq(status)
                .and(addressEq(address))
                .and(genderEq(gender));

        return queryFactory.selectFrom(team)
                .where(expression)
                .fetchCount();
    }

    private BooleanExpression addressEq(String address) {
        return isEmpty(address) ? null : team.address.eq(address);
    }

    private BooleanExpression genderEq(Gender gender) {
        return isEmpty(gender) ? null : team.gender.eq(gender);
    }

    private BooleanExpression statusEq(RecruitmentStatus status) {
        return status.equals(OPEN) ? team.status.eq(status) : team.status.eq(CLOSED);
    }

    @Override
    public List<Team> findSearchByTeam(String teamName) {
        return queryFactory
                .selectFrom(team)
                .where(team.teamName.ne(teamName))
                .fetch();
    }
}
