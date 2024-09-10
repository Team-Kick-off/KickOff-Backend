package com.example.kickoffbackend.match.dto.request;

import com.example.kickoffbackend.match.domain.type.Level;
import com.example.kickoffbackend.match.domain.type.MatchStatus;
import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchCreateRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String matchDate;

    @DateTimeFormat(pattern = "HH:mm")
    private String startTime;

    @Enumerated(EnumType.STRING)
    private Level level;

    private Gender gender;

    @Column(nullable = false)
    private String fieldName;

    private String fieldAddress;

    private String homeTeamName;
    private String awayTeamName;

    private List<Long> teamMemberIdList = new ArrayList<>();

}
