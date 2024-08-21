package com.example.kickoffbackend.team.repository.custom;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import jakarta.transaction.Transactional;

@Transactional
public interface TeamCustom {



    default Team toEntity(TeamCreateRequest request){
        return Team.builder()
                .teamName(request.getTeamName())
                .introduction(request.getIntroduction())
                .rule(request.getRule())
                .fileAttached(0)
                .build();
    }

    default Team toFileEntity(TeamCreateRequest request){
        return Team.builder()
                .teamName(request.getTeamName())
                .introduction(request.getIntroduction())
                .rule(request.getRule())
                .fileAttached(1)
                .build();
    }
}
