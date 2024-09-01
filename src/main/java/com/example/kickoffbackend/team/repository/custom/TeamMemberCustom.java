package com.example.kickoffbackend.team.repository.custom;

import com.example.kickoffbackend.team.domain.Team;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface TeamMemberCustom {

    long countByUserId(Long userId);

    boolean  existsByUserIdAndTeam(Long userId, String teamName);

    List<Team> findByUserId(Long userId);
}
