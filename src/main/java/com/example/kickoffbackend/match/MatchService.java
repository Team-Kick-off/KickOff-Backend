package com.example.kickoffbackend.match;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.*;
import com.example.kickoffbackend.match.domain.type.AcceptStatus;
import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.match.dto.response.MatchResponse;
import com.example.kickoffbackend.match.dto.response.TeamSimpleResponse;
import com.example.kickoffbackend.match.repository.CompeteTeamRepository;
import com.example.kickoffbackend.match.dto.response.TeamMemberSimpleResponse;
import com.example.kickoffbackend.match.repository.MatchRepository;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.dto.response.TeamResponse;
import com.example.kickoffbackend.team.repository.TeamMemberRepository;
import com.example.kickoffbackend.team.repository.TeamRepository;
import com.example.kickoffbackend.user.UserRepository;
import com.example.kickoffbackend.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Transactional(readOnly = true) //⭐
@AllArgsConstructor
@Service
public class MatchService {

    private final AmazonS3Client amazonS3Client;

    @Autowired
    public MatchService(AmazonS3Client amazonS3Client, MatchRepository matchRepository, TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, UserRepository userRepository, CompeteTeamRepository competeTeamRepository, HomeTeamMemberRepository homeTeamMemberRepository, AcceptCompeteRepository acceptCompeteRepository) {
        this.amazonS3Client = amazonS3Client;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
        this.competeTeamRepository = competeTeamRepository;
        this.homeTeamMemberRepository = homeTeamMemberRepository;
        this.acceptCompeteRepository = acceptCompeteRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final CompeteTeamRepository competeTeamRepository;
    private final HomeTeamMemberRepository homeTeamMemberRepository;
    private final AcceptCompeteRepository acceptCompeteRepository;

    public List<TeamSimpleResponse> findTeamList(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        List<Team> teamList = teamMemberRepository.findTeamByUserId(user.getId());
        return teamList.stream()
                .map(team -> {
                    String teamImageUrl = "";
                    if(!team.getTeamImages().isEmpty()) {
                        URL url = amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName());
                        teamImageUrl = url.toString();
                    }
                    return new TeamSimpleResponse().toTeamSimpleInfoResponse(team, teamImageUrl);
                })
                .toList();

    }

    public List<TeamMemberSimpleResponse> findTeamMemberList(String teamName) {

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        // 유저 기본이미지 S3에 저장

        return team.getTeamMembers().stream()
                .map(teamMember -> new TeamMemberSimpleResponse().toTeamMemberSimpleInfoResponse(teamMember))
                .toList();
    }

    public List<TeamSimpleResponse> searchAwayTeamList(String teamName, String keyword) {

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        List<Team> teamList = teamRepository.findSearchByTeam(teamName, keyword);
        return teamList.stream()
                .map(awayTeam -> {
                    String teamImageUrl = "";
                    if(!awayTeam.getTeamImages().isEmpty()) {
                        URL url = amazonS3Client.getUrl(bucket, awayTeam.getTeamImages().get(0).getStoredName());
                        teamImageUrl = url.toString();
                    }
                    return new TeamSimpleResponse().toTeamSimpleInfoResponse(awayTeam, teamImageUrl);
                })
                .toList();
    }

    public MatchResponse createMatch(MatchCreateRequest request) throws IOException {

        Team team = teamRepository.findByName(request.getHomeTeamName())
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        limitMatchCreate(team);
        // 경기장과 경기일정이 이미 있으면 생성 못하도록???

        Match match = matchRepository.toEntity(request);
        matchRepository.save(match);

        CompeteTeam competeTeam = CompeteTeam.builder()
                .competeType(CompeteType.HomeTeam)
                .match(match)
                .team(team)
                .build();
        competeTeamRepository.save(competeTeam);

        team.getTeamMembers().stream()
                .map(teamMember -> {
                    HomeTeamMember homeTeamMember = HomeTeamMember.builder()
                            .nickname(teamMember.getUser().getNickname())
                            .match(match)
                            .build();
                    return homeTeamMemberRepository.save(homeTeamMember);
                        })
                        .toList();

        AcceptCompete acceptCompete = AcceptCompete.builder()
                .match(match)
                .acceptTeamMember(null)
                .status(AcceptStatus.CALL)
                .build();
        acceptCompeteRepository.save(acceptCompete);

        List<TeamMemberSimpleResponse> homeTeamMembers =  team.getTeamMembers().stream()
                .map(teamMember -> new TeamMemberSimpleResponse().toTeamMemberSimpleInfoResponse(teamMember))
                .toList();
        return new MatchResponse().toMatchInfoResponse(match, request.getHomeTeamName(), request.getAwayTeamName(), homeTeamMembers);
    }

    private void limitMatchCreate(Team team) {
        int matchCount = Math.toIntExact(competeTeamRepository.countByTeamName(team.getTeamName()));

        if(matchCount >= 3) {
            throw new IllegalArgumentException("팀당 경기 생성은 3개까지 가능합니다.");
        }
    }
}