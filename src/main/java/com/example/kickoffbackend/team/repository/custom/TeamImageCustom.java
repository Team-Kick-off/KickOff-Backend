package com.example.kickoffbackend.team.repository.custom;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamImage;
import jakarta.transaction.Transactional;

@Transactional
public interface TeamImageCustom {

    default TeamImage toTeamImageEntity(Team team, String originalFileName, String storedFileName){
        return TeamImage.builder()
                .originalName(originalFileName)
                .storedName(storedFileName)
                .team(team)
                .build();
    }
}
