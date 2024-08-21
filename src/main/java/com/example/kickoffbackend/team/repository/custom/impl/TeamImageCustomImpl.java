package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.repository.custom.TeamImageCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class TeamImageCustomImpl implements TeamImageCustom {

    JPAQueryFactory queryFactory;

    public TeamImageCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
