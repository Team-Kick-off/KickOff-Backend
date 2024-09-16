package com.example.kickoffbackend.match;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.*;
import com.example.kickoffbackend.match.domain.type.AcceptStatus;
import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.match.dto.response.MatchResponse;
import com.example.kickoffbackend.team.dto.response.TeamSimpleResponse;
import com.example.kickoffbackend.match.repository.*;
import com.example.kickoffbackend.team.dto.response.TeamMemberSimpleResponse;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
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

@Transactional
@AllArgsConstructor
@Service
public class MatchService {

    private final AmazonS3Client amazonS3Client;

    @Autowired
    public MatchService(AmazonS3Client amazonS3Client, MatchRepository matchRepository, TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, UserRepository userRepository, CompeteTeamRepository competeTeamRepository, CompeteTeamMemberRepository competeTeamMemberRepository, AcceptCompeteRepository acceptCompeteRepository) {
        this.amazonS3Client = amazonS3Client;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userRepository = userRepository;
        this.competeTeamRepository = competeTeamRepository;
        this.competeTeamMemberRepository = competeTeamMemberRepository;
        this.acceptCompeteRepository = acceptCompeteRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final CompeteTeamRepository competeTeamRepository;
    private final CompeteTeamMemberRepository competeTeamMemberRepository;
    private final AcceptCompeteRepository acceptCompeteRepository;

    public MatchResponse createMatch(MatchCreateRequest request, String email) throws IOException {

        Team team = teamRepository.findByName(request.getHomeTeamName())
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        limitMatchCreate(team);
        // 경기장과 경기일정이 이미 있으면 생성 못하도록???

        Match match = matchRepository.toEntity(request);
        matchRepository.save(match);

        CompeteTeam competeHomeTeam = CompeteTeam.builder()
                .competeType(CompeteType.HOME_TEAM)
                .match(match)
                .team(team)
                .build();
        competeTeamRepository.save(competeHomeTeam);

        Team awayTeam = teamRepository.findByName(request.getAwayTeamName())
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));
        CompeteTeam competeAwayTeam = CompeteTeam.builder()
                .competeType(CompeteType.AWAY_TEAM)
                .match(match)
                .team(awayTeam)
                .build();
        competeTeamRepository.save(competeAwayTeam);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        request.getTeamMemberIdList().stream()
                .flatMap(teamMemberId -> teamMemberRepository.findByIdList(teamMemberId).stream()
                        .map(teamMember -> {
                            if(user.getNickname().equals(teamMember.getUser().getNickname())){
                                CompeteTeamMember homeHostTeamMember = CompeteTeamMember.builder()
                                        .nickname(teamMember.getUser().getNickname())
                                        .competeType(CompeteType.HOME_TEAM_HOST)
                                        .match(match)
                                        .build();
                                return competeTeamMemberRepository.save(homeHostTeamMember);
                            }
                            CompeteTeamMember homeTeamMember = CompeteTeamMember.builder()
                                    .nickname(teamMember.getUser().getNickname())
                                    .competeType(CompeteType.HOME_TEAM)
                                    .match(match)
                                    .build();
                            return competeTeamMemberRepository.save(homeTeamMember);
                        })
                ).toList();

        AcceptCompete acceptCompete = AcceptCompete.builder()
                .match(match)
                .acceptTeamMember(null)
                .status(AcceptStatus.CALL)
                .build();
        acceptCompeteRepository.save(acceptCompete);

        List<TeamMemberSimpleResponse> homeTeamMembers = request.getTeamMemberIdList().stream()
                .flatMap(teamMemberId -> {
                    List<TeamMember> teamMembers = teamMemberRepository.findByIdList(teamMemberId);
                    return teamMembers.stream()
                        .map(teamMember -> {
                            return getTeamMemberSimpleResponse(teamMember);
                        });
                }).toList();

        return new MatchResponse().toMatchInfoResponse(match, request.getHomeTeamName(), request.getAwayTeamName(), homeTeamMembers);
    }

    private void limitMatchCreate(Team team) {

        int matchCount = Math.toIntExact(competeTeamRepository.countByTeamName(team.getTeamName()));

        if(matchCount >= 4) {
            throw new IllegalArgumentException("팀당 경기 생성은 3개까지 가능합니다.");
        }
    }

    private TeamMemberSimpleResponse getTeamMemberSimpleResponse(TeamMember teamMember) {
        URL url = amazonS3Client.getUrl(bucket, "기본이미지.png");
        String userImageUrl = url.toString();
        return new TeamMemberSimpleResponse().toTeamMemberSimpleInfoResponse(teamMember, userImageUrl);
    }
}