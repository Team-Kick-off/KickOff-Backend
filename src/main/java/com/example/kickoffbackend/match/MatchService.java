package com.example.kickoffbackend.match;

/*import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.kickoffserver.domain.match.*;
import com.example.kickoffserver.domain.team.Team;
import com.example.kickoffserver.domain.team.TeamMember;
import com.example.kickoffserver.domain.user.User;
import com.example.kickoffserver.dto.match.request.MatchRequest;
import com.example.kickoffserver.dto.match.response.MatchInfoResponse;
import com.example.kickoffserver.dto.match.response.MatchResponse;
import com.example.kickoffserver.dto.match.response.TeamMemberSimpleResponse;
import com.example.kickoffserver.dto.match.response.TeamSimpleInfoResponse;
import com.example.kickoffserver.repository.UserRepository;
import com.example.kickoffserver.repository.match.*;
import com.example.kickoffserver.repository.team.TeamMemberRepository;
import com.example.kickoffserver.repository.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;*/

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

//@Transactional(readOnly = true) //⭐
@AllArgsConstructor
@Service
public class MatchService {
/*

    private final AmazonS3Client amazonS3Client;

    @Autowired
    public MatchService(AmazonS3Client amazonS3Client, MatchRepository matchRepository, TeamRepository teamRepository, FieldRepository fieldRepository, FieldImageRepository fieldImageRepository, TeamMemberRepository teamMemberRepository, CompeteTeamRepository competeTeamRepository, UserRepository userRepository) {
        this.amazonS3Client = amazonS3Client;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.fieldRepository = fieldRepository;
        this.fieldImageRepository = fieldImageRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.competeTeamRepository = competeTeamRepository;
        this.userRepository = userRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final FieldRepository fieldRepository;
    private final FieldImageRepository fieldImageRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final CompeteTeamRepository competeTeamRepository;
    private final UserRepository userRepository;

    public List<TeamSimpleInfoResponse> findTeamListMatch(String username) throws IOException {

        User user = userRepository.findByUsername(username);

        List<TeamMember> teamList = teamMemberRepository.findAllByUserID(user.getId());
        List<TeamSimpleInfoResponse> toTeamResponse = new ArrayList<>();
        for (TeamMember userTeam : teamList) {
            // TODO : 팀 이미지 null(이거나 기본이미지?)일때 구분해서 로직
            URL url = amazonS3Client.getUrl(bucket, userTeam.getTeam().getTeamImages().get(0).getStoredFileName());
            toTeamResponse.add(TeamSimpleInfoResponse.toTeamMemberResponse(userTeam, url));
        }

        return toTeamResponse;
    }

    public List<TeamMemberSimpleResponse> findTeamMemberListMatch(Long teamId) {
        List<TeamMember> teamMemberList = teamMemberRepository.findAllByTeamId(teamId);
        List<TeamMemberSimpleResponse> toTeamMemberResponse = new ArrayList<>();
        for (TeamMember teamMember : teamMemberList) {
            URL userUrl = amazonS3Client.getUrl(bucket, "기본이미지.png");
            toTeamMemberResponse.add(TeamMemberSimpleResponse.toTeamMemberResponse(teamMember, userUrl));
        }

        return toTeamMemberResponse;
    }

    public List<TeamSimpleInfoResponse> findCompeteTeamMatch() {
        // TODO : compete과 awayTeamMember 상대팀 생성됨
        List<Team> teamAllList = teamRepository.findAll();
        List<TeamSimpleInfoResponse> toTeamResponse = new ArrayList<>();
        for(Team teamList : teamAllList) {
            URL teamUrl = amazonS3Client.getUrl(bucket, teamList.getTeamImages().get(0).getStoredFileName());
            toTeamResponse.add(TeamSimpleInfoResponse.toTeamResponse(teamList, teamUrl));
        }

        return toTeamResponse;
    }

    public String createMatch(MatchRequest request) throws IOException {
        Team team = teamRepository.findByName(request.getTeamName()).orElseThrow(IllegalArgumentException::new);

        duplicatedInfosCheck(request);
        limitMatchCreate(team);

        if (request.getFieldFile() == null || request.getFieldFile().isEmpty()) {

            // 이미지 미첨부(랜덤 이미지 첨부됨)
            Field createField = Field.createField(request);
            Field field = fieldRepository.save(createField);

            MatchRecord createRecord = MatchRecord.createMatchRecord();
            MatchRecord record = matchRecordRepository.save(createRecord);
            Compete createCompete = Compete.defaltCompete(record);
            Compete compete = competeRepository.save(createCompete);
            for (Long teamMemberId : request.getTeamMemberId()) {
                TeamMember teamMember = teamMemberRepository.findTeamMemberById(teamMemberId);
                homeTeamMemberRepository.save(HomeTeamMember.createHomeTeam(compete, teamMember));
            }

            Match match = Match.createMatch(request, field, team, record);
            matchRepository.save(match);

            public static Match createMatch(MatchRequest request, Field field, Team team, MatchRecord record) {
                return Match.builder()
                        .description(request.getDescription())
                        .participantsCount(request.getParticipantsCount())
                        .matchDate(LocalDate.parse(request.getMatchDate()))
                        .startTime(LocalTime.parse(request.getStartTime()))
                        .endTime(null)
                        .cost(request.getCost())
                        .level(Level.valueOf(request.getLevel()))
                        .genderType(GenderType.valueOf(request.getGenderType()))
                        .type(Type.valueOf(request.getType()))
                        .status(MatchStatus.RECRUITING)
                        .field(field)
                        .team(team)
                        .record(record)
                        .build();
            }

            return "경기장 생성 및 경기 주최 완료";

        }

            Field createField = Field.createImagesField(request);
            Field field = fieldRepository.save(createField);

            for (MultipartFile fieldFile : request.getFieldFile()) {
                String originalFileName = fieldFile.getOriginalFilename();
                String storedFileName = UUID.randomUUID() + "_" + originalFileName;

                // S3에 업로드 할 파일의 메타데이터 생성
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(fieldFile.getContentType());
                metadata.setContentLength(fieldFile.getSize());
                // S3에 파일 업로드
                amazonS3Client.putObject(bucket, storedFileName, fieldFile.getInputStream(), metadata);
                System.out.println("s3 url 주소 반환 : " + amazonS3Client.getUrl(bucket, storedFileName).toString());

                FieldImage fieldImage = FieldImage.createFieldImages(field, originalFileName, storedFileName);
                fieldImageRepository.save(fieldImage);
            }

            MatchRecord createRecord = MatchRecord.createMatchRecord();
            MatchRecord record = matchRecordRepository.save(createRecord);
            Compete createCompete = Compete.defaltCompete(record);
            Compete compete = competeRepository.save(createCompete);
            for (Long teamMemberId : request.getTeamMemberId()) {
                TeamMember teamMember = teamMemberRepository.findTeamMemberById(teamMemberId);
                homeTeamMemberRepository.save(HomeTeamMember.createHomeTeam(compete, teamMember));
            }

            //TODO : 상대팀 아이디 받은걸로 AwayTeamMember로 상대팀팀장찾아서넣어주기?

            Match match = Match.createMatch(request, field, team, record);
            matchRepository.save(match);

        public static Match createMatch(MatchRequest request, Field field, Team team, MatchRecord record) {
            return Match.builder()
                    .description(request.getDescription())
                    .participantsCount(request.getParticipantsCount())
                    .matchDate(LocalDate.parse(request.getMatchDate()))
                    .startTime(LocalTime.parse(request.getStartTime()))
                    .endTime(null)
                    .cost(request.getCost())
                    .level(Level.valueOf(request.getLevel()))
                    .genderType(GenderType.valueOf(request.getGenderType()))
                    .type(Type.valueOf(request.getType()))
                    .status(MatchStatus.RECRUITING)
                    .field(field)
                    .team(team)
                    .record(record)
                    .build();
        }

            return "이미지 첨부 및 경기 생성 완료";

    }

    private void duplicatedInfosCheck(MatchRequest request) {
        if (!(teamRepository.existsByName(request.getTeamName()))) {
            throw new IllegalArgumentException(request.getTeamName()+"팀은 존재 하지 않습니다.");
        }
        for(Long teamMemberId : request.getTeamMemberId()) {
            if (teamMemberRepository.findTeamMemberById(teamMemberId) == null) {
                throw new IllegalArgumentException(teamMemberId+"회원은 존재 하지 않습니다.");
            }
        }
    }

    private void limitMatchCreate(Team team){
        int matchCount = matchRepository.countMatchByTeamId(team.getId()); // TODO : homeTeamMember로 연관해서 3개까지로 제한...?

        if(matchCount >= 3){
            throw new IllegalArgumentException("팀당 경기 생성은 3개까지 가능합니다.");
        }
    }
*/

}