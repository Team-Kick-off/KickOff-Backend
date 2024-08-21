package com.example.kickoffbackend.team.repository.custom;

import jakarta.transaction.Transactional;

@Transactional
public interface TeamMemberCustom {

    long countByUserId(Long userId);

}
