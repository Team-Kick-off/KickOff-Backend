package com.example.kickoffbackend.team.dto.request;

import com.example.kickoffbackend.team.domain.Gender;
import com.example.kickoffbackend.team.domain.RecruitmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamFilterRequest {
    private String address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus status;
}
