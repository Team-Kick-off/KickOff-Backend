package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchCustom {
}
