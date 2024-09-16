package com.example.kickoffbackend.team.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.team.dto.response.TeamMemberSimpleResponse;
import com.example.kickoffbackend.team.dto.response.TeamSimpleResponse;
import com.example.kickoffbackend.team.domain.*;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.dto.response.TeamFilterResponse;
import com.example.kickoffbackend.team.dto.response.TeamMemberResponse;
import com.example.kickoffbackend.team.dto.response.TeamResponse;
import com.example.kickoffbackend.team.repository.TeamImageRepository;
import com.example.kickoffbackend.team.repository.TeamMemberRepository;
import com.example.kickoffbackend.team.repository.TeamRepository;
import com.example.kickoffbackend.user.UserRepository;
import com.example.kickoffbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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
        User user = result.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

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

    public TeamResponse getTeamInfo(String teamName) {

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        String teamImageUrl = "";
        if(!team.getTeamImages().isEmpty()){
            URL url = amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName());
            teamImageUrl = url.toString();
        }

        Set<TeamMember> members = team.getTeamMembers();

        List<TeamMemberResponse> teamMemberResponses = members.stream()
                .map(teamMember -> new TeamMemberResponse(teamMember.getUser().getName(), teamMember.getRole()))
                .collect(Collectors.toList());

        return new TeamResponse().toTeamInfoResponse(team, teamImageUrl, teamMemberResponses);
    }

    public String teamRegister(String teamName, String email, String teamRegisterRequest) {

        Optional<User> userResult = userRepository.findByEmail(email);

        User user = userResult.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        Optional<Team> teamResult = teamRepository.findByTeamName(teamName);

        Team team = teamResult.orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        if(teamMemberRepository.existsByUserIdAndTeam(user.getId(), teamName)){
            throw new ApiException(ErrorCode.ALREADY_JOINED_TEAM_ERROR);
        }

        TeamMember teamMember = TeamMember.builder()
                        .user(user)
                        .team(team)
                        .teamRequestContent(teamRegisterRequest)
                        .role(Role.MEMBER)
                        .build();

        teamMemberRepository.save(teamMember);

        return  "팀 가입이 완료되었습니다.";
    }


    @Transactional(readOnly = true)
    public Page<TeamFilterResponse> findTeamFilter(String address, Gender gender, RecruitmentStatus status, Pageable pageable){

        List<Team> teams =  teamRepository.findByTeamFilter(address, gender, status, pageable);

        List<TeamFilterResponse> teamFilterResponses = teams.stream()
                .map(team -> {
                    String teamImageUrl = "";
                    if (!team.getTeamImages().isEmpty()) {
                        URL url = amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName());
                        teamImageUrl = url.toString();
                    }
                    return new TeamFilterResponse().toTeamFilterResponse(team, teamImageUrl);
                })
                .collect(Collectors.toList());

        long total = teamRepository.countByTeamFilter(address, gender, status);
        return new PageImpl<>(teamFilterResponses, pageable, total);
    }

    public boolean isTeamNameDuplicate(String teamName) {
        Optional<Team> result = teamRepository.findByTeamName(teamName);

        return result.isPresent();
    }

    /**
     * Match 관련 조회 Service
     * Description : 경기_생성, 경기_수락, 경기_운영관리 관련
     **/

    public List<TeamSimpleResponse> findTeamByUser(String email) { // 경기 생성_로그인 유저의 팀들 조회

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        List<Team> teamList = teamMemberRepository.findTeamByUserId(user.getId());
        return teamList.stream()
                .map(team -> {
                    String teamImageUrl = getTeamImageUrl(team);
                    return new TeamSimpleResponse().toTeamSimpleInfoResponse(team, teamImageUrl);
                })
                .toList();
    }

    public TeamSimpleResponse findTeamMemberList(String teamName) { // 경기 생성_선택한 팀의 팀원들 조회

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        String teamImageUrl = getTeamImageUrl(team);

        List<TeamMemberSimpleResponse> teamMembers =  team.getTeamMembers().stream()
                .map(teamMember -> {
                    return getTeamMemberSimpleResponse(teamMember);
                })
                .toList();

        return new TeamSimpleResponse().toTeamSimpleResponse(team, teamImageUrl, teamMembers);
    }

    public List<TeamSimpleResponse> searchAwayTeamList(String teamName) { // 경기 생성_상대팀 검색 조회

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        List<Team> teamList = teamRepository.findSearchByTeam(teamName);
        return teamList.stream()
                .map(awayTeam -> {
                    String teamImageUrl = getTeamImageUrl(awayTeam);
                    return new TeamSimpleResponse().toTeamSimpleInfoResponse(awayTeam, teamImageUrl);
                })
                .toList();
    }

    private String getTeamImageUrl(Team team) {
        String teamImageUrl = "";
        if(!team.getTeamImages().isEmpty()){
            URL url = amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName());
            teamImageUrl = url.toString();
        }
        return teamImageUrl;
    }

    private TeamMemberSimpleResponse getTeamMemberSimpleResponse(TeamMember teamMember) {
        URL url = amazonS3Client.getUrl(bucket, "기본이미지.png");
        String userImageUrl = url.toString();
        return new TeamMemberSimpleResponse().toTeamMemberSimpleInfoResponse(teamMember, userImageUrl);
    }
}
