package com.example.kickoffbackend.team.repository.custom.impl;

import com.example.kickoffbackend.team.repository.custom.TeamCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class TeamCustomImpl implements TeamCustom {

    JPAQueryFactory queryFactory;

    public TeamCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


}
