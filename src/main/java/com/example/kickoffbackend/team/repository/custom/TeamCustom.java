package com.example.kickoffbackend.team.repository.custom;

import com.example.kickoffbackend.team.domain.Gender;
import com.example.kickoffbackend.team.domain.RecruitmentStatus;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.dto.request.TeamFilterRequest;
import com.example.kickoffbackend.team.dto.response.TeamResponse;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface TeamCustom {
    Optional<Team> findByName(String teamName);

    List<Team> findByTeamFilter(String address, Gender gender, RecruitmentStatus status);

    default Team toEntity(TeamCreateRequest request){
        return Team.builder()
                .teamName(request.getTeamName())
                .teamIntroduction(request.getTeamIntroduction())
                .teamLevel(request.getTeamLevel())
                .address(request.getAddress())
                .gender(request.getGender())
                .teamRule(request.getTeamRule())
                .fileAttached(0)
                .build();
    }

    default Team toFileEntity(TeamCreateRequest request){
        return Team.builder()
                .teamName(request.getTeamName())
                .teamIntroduction(request.getTeamIntroduction())
                .teamLevel(request.getTeamLevel())
                .address(request.getAddress())
                .gender(request.getGender())
                .teamRule(request.getTeamRule())
                .fileAttached(1)
                .build();
    }

    default TeamResponse toDto(Team team){
        TeamResponse teamResponse = TeamResponse.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .teamIntroduction(team.getTeamIntroduction())
                .teamLevel(team.getTeamLevel())
                .address(team.getAddress())
                .gender(team.getGender())
                .teamRule(team.getTeamRule())
                .build();

        if(team.getFileAttached() == 0){
            teamResponse.setFileAttached(0);
        } else {
            teamResponse.setFileAttached(team.getFileAttached());
            teamResponse.setOriginalTeamImageName(teamResponse.getOriginalTeamImageName());
            teamResponse.setStoredTeamImageName(teamResponse.getStoredTeamImageName());
        }
        return teamResponse;
    }

    List<Team> findSearchByTeam(String teamName, String search);
}
