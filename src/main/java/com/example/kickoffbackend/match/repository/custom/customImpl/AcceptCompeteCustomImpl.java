package com.example.kickoffbackend.match.repository.custom.customImpl;

import com.example.kickoffbackend.match.domain.AcceptCompete;
import com.example.kickoffbackend.match.repository.custom.AcceptCompeteCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.example.kickoffbackend.match.domain.QAcceptCompete.acceptCompete;
import static com.example.kickoffbackend.match.domain.QMatch.match;

public class AcceptCompeteCustomImpl implements AcceptCompeteCustom {

    JPAQueryFactory queryFactory;

    public AcceptCompeteCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public AcceptCompete findByMatchId(Long matchId) {
        return queryFactory.selectFrom(acceptCompete)
                .join(acceptCompete.match, match)
                .where(acceptCompete.match.id.eq(matchId))
                .fetchOne();
    }

}
