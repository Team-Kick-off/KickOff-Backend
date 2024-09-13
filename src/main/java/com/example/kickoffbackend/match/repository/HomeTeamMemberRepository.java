package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.HomeTeamMember;
import com.example.kickoffbackend.match.repository.custom.HomeTeamMemberCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeTeamMemberRepository extends JpaRepository<HomeTeamMember, Long>, HomeTeamMemberCustom {
}