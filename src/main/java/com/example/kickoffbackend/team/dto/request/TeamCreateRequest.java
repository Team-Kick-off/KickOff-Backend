package com.example.kickoffbackend.team.dto.request;

import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamCreateRequest {

    private Long id;

    private String teamName;

    private String teamIntroduction;

    private String teamRule;

    private MultipartFile teamFile; // 파일 담는 용도

    private int fileAttached;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String teamLevel;
}
