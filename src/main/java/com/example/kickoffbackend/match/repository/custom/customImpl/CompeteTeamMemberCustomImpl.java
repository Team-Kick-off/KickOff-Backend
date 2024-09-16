package com.example.kickoffbackend.match.repository.custom.customImpl;

import com.example.kickoffbackend.match.repository.custom.CompeteTeamMemberCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CompeteTeamMemberCustomImpl implements CompeteTeamMemberCustom {

    JPAQueryFactory queryFactory;

    public CompeteTeamMemberCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


}
