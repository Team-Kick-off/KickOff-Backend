package com.example.kickoffbackend.team.dto.response;


import com.example.kickoffbackend.team.domain.Gender;
import com.example.kickoffbackend.team.domain.RecruitmentStatus;
import com.example.kickoffbackend.team.domain.Team;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamFilterResponse {

    private Long id;

    private String teamName;

    private String teamIntroduction;

    private String teamRule;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String teamLevel;

    private String teamImageUrl;

    private RecruitmentStatus status;

    public TeamFilterResponse toTeamFilterResponse(Team team, String imageUrl){
        return TeamFilterResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamIntroduction(team.getTeamIntroduction())
                .teamRule(team.getTeamRule())
                .gender(team.getGender())
                .address(team.getAddress())
                .teamLevel(team.getTeamLevel())
                .teamImageUrl(imageUrl)
                .status(team.getStatus())
                .build();

    }

}
