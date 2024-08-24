package com.example.kickoffbackend.team.repository;

import com.example.kickoffbackend.team.domain.TeamImage;
import com.example.kickoffbackend.team.repository.custom.TeamImageCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamImageRepository extends JpaRepository<TeamImage, Long>, TeamImageCustom {
}
