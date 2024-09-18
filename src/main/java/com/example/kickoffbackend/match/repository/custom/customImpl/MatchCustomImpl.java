package com.example.kickoffbackend.match.repository.custom.customImpl;

import com.example.kickoffbackend.match.repository.custom.MatchCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.kickoffbackend.match.domain.QMatch.match;

public class MatchCustomImpl implements MatchCustom {

    JPAQueryFactory queryFactory;

    public MatchCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean findByField(LocalDate matchDate, LocalTime startTime, String fieldName) {
        return queryFactory.selectFrom(match)
                .where(match.matchDate.eq(matchDate)
                        .and(match.startTime.eq(startTime))
                        .and(match.fieldName.eq(fieldName)))
                .fetchFirst() != null;
    }

}
