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
public class TeamMemberSimpleInfoResponse {

    private Long id;

    private String teamMemberNickname;

    public TeamMemberSimpleInfoResponse(String teamMemberNickname) {
        this.teamMemberNickname = teamMemberNickname;
    }

//    private String teamMemberImageUrl;

//    public static TeamMemberSimpleInfoResponse toTeamMemberResponse(TeamMember teamMember) {
//        return TeamMemberSimpleInfoResponse.builder()
//                .teamMemberNickname(teamMember.getUser().getNickname())
//                .teamMemberImageUrl(teamMemberImageUrl)
//                .build();
//    }
}
