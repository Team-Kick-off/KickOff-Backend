package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.AwayTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwayTeamMemberRepository extends JpaRepository<AwayTeamMember, Long> {
}
