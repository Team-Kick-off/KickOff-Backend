package com.example.kickoffbackend.team.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final AmazonS3Client amazonS3Client;

    private final TeamRepository teamRepository;

    private final TeamImageRepository teamImageRepository;

    private final UserRepository userRepository;

    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamService(AmazonS3Client amazonS3Client, TeamImageRepository teamImageRepository, TeamMemberRepository teamMemberRepository, TeamRepository teamRepository, UserRepository userRepository){
        this.amazonS3Client = amazonS3Client;
        this.teamImageRepository = teamImageRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String create(TeamCreateRequest teamCreateRequest, String email) throws IOException {

        Optional<User> result = userRepository.findByEmail(email);
        User user = result.orElseThrow();

        limitTeamCreate(user);
        duplicateCheckTeamName(teamCreateRequest.getTeamName());

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
//            String savePath = "/Users/baegseungchan/Desktop/imageTest/" + storedFileName;
//            teamFile.transferTo(new File(savePath)); // 파일 저장

            // S3에 업로드 할 파일의 메타데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(teamFile.getContentType());
            metadata.setContentLength(teamFile.getSize());

            // S3에 업로드
            amazonS3Client.putObject(bucket, storedFileName, teamFile.getInputStream(), metadata);

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

    private void limitTeamCreate(User user){
        int teamCount = (int) teamMemberRepository.countByUserId(user.getId());

        if(teamCount >= 50){
            throw new IllegalStateException("유저당 팀 생성은 3개까지 가능합니다.");
        }
    }


    private void duplicateCheckTeamName(String teamName){
        if(teamRepository.findByTeamName(teamName).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 팀명입니다.");
        }
    }
}