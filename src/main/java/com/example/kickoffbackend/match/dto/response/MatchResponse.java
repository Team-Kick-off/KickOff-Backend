package com.example.kickoffbackend.match.dto.response;

import com.example.kickoffbackend.match.domain.HomeTeamMember;
import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.match.domain.type.Level;
import com.example.kickoffbackend.team.domain.Gender;
import com.example.kickoffbackend.team.domain.TeamMember;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {

    private Long id;

    private LocalDate matchDate;

    private LocalTime startTime;

    private String fieldName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String homeTeamName;

    private String awayTeamName;

    private List<TeamMemberSimpleResponse> homeTeamMembers;

    public MatchResponse toMatchInfoResponse(Match match, String homeTeamName, String awayTeamName, List<TeamMemberSimpleResponse> homeTeamMembers) {
        return MatchResponse.builder()
                .id(match.getId())
                .matchDate(match.getMatchDate())
                .startTime(match.getStartTime())
                .fieldName(match.getFieldName())
                .gender(match.getGender())
                .level(match.getLevel())
                .homeTeamName(homeTeamName)
                .awayTeamName(awayTeamName)
                .homeTeamMembers(homeTeamMembers)
                .build();
    }
}
