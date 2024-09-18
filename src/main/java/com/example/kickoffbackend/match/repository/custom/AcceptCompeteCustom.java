package com.example.kickoffbackend.match.repository.custom;

import com.example.kickoffbackend.match.domain.AcceptCompete;
import com.example.kickoffbackend.team.domain.Team;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AcceptCompeteCustom {

    AcceptCompete findByMatchId(Long matchId);
}
