package com.example.kickoffbackend.team.service;

import com.example.kickoffbackend.team.domain.Role;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamImage;
import com.example.kickoffbackend.team.domain.TeamMember;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.repository.TeamImageRepository;
import com.example.kickoffbackend.team.repository.TeamMemberRepository;
import com.example.kickoffbackend.team.repository.TeamRepository;
import com.example.kickoffbackend.user.UserRepository;
import com.example.kickoffbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamImageRepository teamImageRepository;

    private final UserRepository userRepository;

    private final TeamMemberRepository teamMemberRepository;

    public String create(TeamCreateRequest teamCreateRequest, String email) throws IOException {

        Optional<User> result = userRepository.findByEmail(email);
        User user = result.orElseThrow();

        if(teamCreateRequest.getTeamFile() == null || teamCreateRequest.getTeamFile().isEmpty()){
            // 첨부 파일 없음

            Team team = teamRepository.toEntity(teamCreateRequest);
            teamRepository.save(team);

            TeamMember teamMember = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .role(Role.ADMIN)
                    .build();

            teamMemberRepository.save(teamMember);

            return "팀 생성 및 관리자 역할이 설정되었습니다.";
        } else {
            // 첨부 파일 있음
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름을 만듦
                // 사진.jpg => UUID_사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. team_table에 저장
                7. team_file_table에 저장
            */

            MultipartFile teamFile = teamCreateRequest.getTeamFile();
            String originalFileName = teamFile.getOriginalFilename();
            String storedFileName = UUID.randomUUID() + "_" + originalFileName;
            String savePath = "/Users/baegseungchan/Desktop/imageTest/" + storedFileName;
            teamFile.transferTo(new File(savePath)); // 파일 저장

            Team teamEntity = teamRepository.toFileEntity(teamCreateRequest);
            Team team = teamRepository.save(teamEntity);

            TeamImage teamImage = teamImageRepository.toTeamImageEntity(team, originalFileName, storedFileName);
            teamImageRepository.save(teamImage);

            TeamMember teamMember = TeamMember.builder()
                    .user(user)
                    .team(team)
                    .role(Role.ADMIN)
                    .build();

            teamMemberRepository.save(teamMember);

            return "파일 첨부 및 팀 생성이 설정되었습니다.";
        }
    }
}
