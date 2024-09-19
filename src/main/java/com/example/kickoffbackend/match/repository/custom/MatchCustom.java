package com.example.kickoffbackend.match.repository.custom;

import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Transactional
public interface MatchCustom {

    default Match toEntity(MatchCreateRequest request) {
        return Match.builder()
                .matchDate(LocalDate.parse(request.getMatchDate()))
                .startTime(LocalTime.parse(request.getStartTime()))
                .fieldName(request.getFieldName())
                .fieldAddress(request.getFieldAddress())
                .level(request.getLevel())
                .gender(request.getGender())
                .build();
    }

    boolean findByField(LocalDate matchDate, LocalTime startTime, String fieldName);
}
