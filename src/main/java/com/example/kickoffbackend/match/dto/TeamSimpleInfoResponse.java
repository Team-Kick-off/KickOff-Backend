package com.example.kickoffbackend.match.dto;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamSimpleInfoResponse {

    private Long id;

    private String teamName;

    private String teamImageUrl;

    public TeamSimpleInfoResponse(String teamName, String teamImageUrl) {
        this.teamName = teamName;
        this.teamImageUrl = teamImageUrl;
    }

    public static TeamSimpleInfoResponse toTeamResponse(Team team, String teamImageUrl) {
        return TeamSimpleInfoResponse.builder()
                .teamName(team.getTeamName())
                .teamImageUrl(teamImageUrl)
                .build();
    }

}
