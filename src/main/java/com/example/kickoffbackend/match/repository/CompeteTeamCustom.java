package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.CompeteTeam;
import com.example.kickoffbackend.team.domain.Team;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CompeteTeamCustom {

    Long countByTeamName(String teamName);

    Team findHomeTeamByMatchId(Long matchId);

    Team findAwayTeamByMatchId(Long matchId);
}
