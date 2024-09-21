package com.example.kickoffbackend.match.repository.custom;

import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.team.domain.Team;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CompeteTeamCustom {

    Long countByTeamName(String teamName);

    Optional<Team> findHomeTeamByMatchId(Long matchId);

    Optional<Team> findAwayTeamByMatchId(Long matchId);

    List<Match> findMatchByTeamId(Long teamId);
}
