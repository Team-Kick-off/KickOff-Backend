package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.CompeteTeam;
import com.example.kickoffbackend.match.repository.custom.CompeteTeamCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompeteTeamRepository extends JpaRepository<CompeteTeam, Long>, CompeteTeamCustom {
}