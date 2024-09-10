package com.example.kickoffbackend.match.dto.response;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.dto.response.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamSimpleResponse {

    private Long id;

    private String teamName;

    private String teamImageUrl;


    public TeamSimpleResponse toTeamSimpleInfoResponse(Team team, String teamImageUrl) {
        return TeamSimpleResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamImageUrl(teamImageUrl)
                .build();
    }
}
