package com.example.kickoffbackend.team.repository.custom;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface TeamMemberCustom {

    long countByUserId(Long userId);

    boolean  existsByUserIdAndTeam(Long userId, String teamName);

    List<Team> findTeamByUserId(Long userId);

    TeamMember findTeamMemberByUserId(Long userId);

    List<TeamMember> findByIdList(Long teamMemberId);
}
