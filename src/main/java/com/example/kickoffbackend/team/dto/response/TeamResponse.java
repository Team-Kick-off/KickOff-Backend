package com.example.kickoffbackend.team.dto.response;

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

    private String introduction;

    private String rule;

    private MultipartFile teamFile; // 파일 담는 용도
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 저장된 파일 이름
    private int fileAttached; // 파일 이름 여부(첨부 1, 미첨부 0)

}
