package com.example.kickoffbackend.team.dto.response;

import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {

    private Long id;

    private String teamName;

    private String teamIntroduction;

    private String teamRule;

    private int fileAttached;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String teamLevel;

    private String originalTeamImageName;

    private String storedTeamImageName;

}
