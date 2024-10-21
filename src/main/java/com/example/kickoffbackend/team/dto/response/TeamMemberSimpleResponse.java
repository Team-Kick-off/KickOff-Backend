package com.example.kickoffbackend.team.dto.response;

import com.example.kickoffbackend.team.domain.Role;
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

    private Role role;

    public static TeamMemberSimpleResponse toTeamMemberSimpleResponse(TeamMember teamMember, String userImageUrl) {
        return TeamMemberSimpleResponse.builder()
                .id(teamMember.getId())
                .teamMemberNickname(teamMember.getUser().getNickname())
                .teamMemberImageUrl(userImageUrl)
                .role(teamMember.getRole())
                .build();
    }
}
