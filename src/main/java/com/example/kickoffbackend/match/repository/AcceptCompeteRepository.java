package com.example.kickoffbackend.match.repository;

import com.example.kickoffbackend.match.domain.AcceptCompete;
import com.example.kickoffbackend.match.repository.custom.AcceptCompeteCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcceptCompeteRepository extends JpaRepository<AcceptCompete, Long>, AcceptCompeteCustom {
}