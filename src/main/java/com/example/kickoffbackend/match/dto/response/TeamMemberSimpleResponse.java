package com.example.kickoffbackend.match.dto.response;

import com.example.kickoffbackend.team.domain.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberSimpleResponse {

    private Long id;

    private String teamMemberNickname;

    private String teamMemberImageUrl;


    public static TeamMemberSimpleResponse toTeamMemberSimpleInfoResponse(TeamMember teamMember, String userImageUrl) {
        return TeamMemberSimpleResponse.builder()
                .id(teamMember.getId())
                .teamMemberNickname(teamMember.getUser().getNickname())
                .teamMemberImageUrl(userImageUrl)
                .build();
    }
}
