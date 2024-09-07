package com.example.kickoffbackend.match.domain;

import com.example.kickoffbackend.match.repository.HomeTeamMemberCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeTeamMemberRepository extends JpaRepository<HomeTeamMember, Long>, HomeTeamMemberCustom {
}