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

    private Long homeTeamId;

    private String homeTeamName;

    private String homeTeamImageUrl;

    private List<TeamMemberSimpleResponse> homeTeamMembers;

    private Long awayTeamId;

    private String awayTeamName;

    private String awayTeamImageUrl;

    private List<TeamMemberSimpleResponse> awayTeamMembers;

    public TeamSimpleResponse toTeamSimpleResponse(Team team, String teamImageUrl) {
        return TeamSimpleResponse.builder()
                .homeTeamId(team.getId())
                .homeTeamName(team.getTeamName())
                .homeTeamImageUrl(teamImageUrl)
                .build();
    }

    public TeamSimpleResponse toTeamSimpleResponse(Team team, String teamImageUrl, List<TeamMemberSimpleResponse> teamMembers) {
        return TeamSimpleResponse.builder()
                .homeTeamId(team.getId())
                .homeTeamName(team.getTeamName())
                .homeTeamImageUrl(teamImageUrl)
                .homeTeamMembers(teamMembers)
                .build();
    }

    public TeamSimpleResponse toTeamSimpleResponse(Team homeTeam, String homeTeamImageUrl, List<TeamMemberSimpleResponse> homeTeamMembers, Team awayTeam, String awayTeamImageUrl, List<TeamMemberSimpleResponse> awayTeamMembers) {
        return TeamSimpleResponse.builder()
                .homeTeamId(homeTeam.getId())
                .homeTeamName(homeTeam.getTeamName())
                .homeTeamImageUrl(homeTeamImageUrl)
                .homeTeamMembers(homeTeamMembers)
                .awayTeamId(awayTeam.getId())
                .awayTeamName(awayTeam.getTeamName())
                .awayTeamImageUrl(awayTeamImageUrl)
                .awayTeamMembers(awayTeamMembers)
                .build();
    }
}
