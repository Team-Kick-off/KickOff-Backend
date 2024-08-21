package com.example.kickoffbackend.team.repository;

import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.repository.custom.TeamCustom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamCustom {

    Optional<Team> findByTeamName(String teamName);
}
