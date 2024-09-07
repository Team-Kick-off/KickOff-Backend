package com.example.kickoffbackend.match.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class HomeTeamMemberCustomImpl implements HomeTeamMemberCustom{

    JPAQueryFactory queryFactory;

    public HomeTeamMemberCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


}
