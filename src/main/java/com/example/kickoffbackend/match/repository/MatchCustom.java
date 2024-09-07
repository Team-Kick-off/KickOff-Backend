package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.team.domain.Team;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
}
