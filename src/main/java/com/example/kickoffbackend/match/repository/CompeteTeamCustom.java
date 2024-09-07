package com.example.kickoffbackend.match.repository;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CompeteTeamCustom {

    Long countByTeamName(String teamName);
}
