package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.repository.custom.TeamMemberCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class TeamMemberCustomImpl implements TeamMemberCustom {

    JPAQueryFactory queryFactory;

    public TeamMemberCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

}
