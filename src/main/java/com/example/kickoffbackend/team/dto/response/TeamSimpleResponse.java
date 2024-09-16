package com.example.kickoffbackend.team.dto.response;

import com.example.kickoffbackend.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamSimpleResponse {

    private Long id;

    private String teamName;

    private String teamImageUrl;

    private List<TeamMemberSimpleResponse> teamMembers;

    public TeamSimpleResponse toTeamSimpleInfoResponse(Team team, String teamImageUrl) {
        return TeamSimpleResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamImageUrl(teamImageUrl)
                .build();
    }

    public TeamSimpleResponse toTeamSimpleResponse(Team team, String teamImageUrl, List<TeamMemberSimpleResponse> teamMembers) {
        return TeamSimpleResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamImageUrl(teamImageUrl)
                .teamMembers(teamMembers)
                .build();
    }
}
