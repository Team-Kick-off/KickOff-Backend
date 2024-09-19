package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.CompeteTeamMember;
import com.example.kickoffbackend.match.repository.custom.CompeteTeamMemberCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompeteTeamMemberRepository extends JpaRepository<CompeteTeamMember, Long>, CompeteTeamMemberCustom {
}