package com.example.kickoffbackend.team.repository;

import com.example.kickoffbackend.team.domain.TeamMember;
import com.example.kickoffbackend.team.repository.custom.TeamMemberCustom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberCustom {

}
