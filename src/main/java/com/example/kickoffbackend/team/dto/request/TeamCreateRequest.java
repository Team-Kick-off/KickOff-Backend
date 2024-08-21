package com.example.kickoffbackend.team.dto.request;

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

    private String introduction;

    private String rule;

    private MultipartFile teamFile; // 파일 담는 용도

    private int fileAttached;
}
