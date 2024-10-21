package com.example.kickoffbackend.match;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.*;
import com.example.kickoffbackend.match.domain.type.AcceptStatus;
import com.example.kickoffbackend.match.domain.type.CompeteType;
import com.example.kickoffbackend.match.domain.type.MatchStatus;
import com.example.kickoffbackend.match.dto.request.AcceptTeamMemberRequest;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.match.dto.response.MatchResponse;
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
import java.time.LocalDate;
import java.time.LocalTime;
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
        duplicateCheckField(LocalDate.parse(request.getMatchDate()), LocalTime.parse(request.getStartTime()), request.getFieldName());

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

        return new MatchResponse().toMatchResponse(match, request.getHomeTeamName(), request.getAwayTeamName(), homeTeamMembers);
    }

    private void limitMatchCreate(Team team) {

        int matchCount = Math.toIntExact(competeTeamRepository.countByTeamName(team.getTeamName()));

        if(matchCount >= 4) {
            throw new IllegalArgumentException("팀당 경기 생성은 3개까지 가능합니다.");
        }
    }

    private void duplicateCheckField(LocalDate matchDate, LocalTime startTime, String fieldName) {
        if(matchRepository.findByField(matchDate, startTime, fieldName)) {
            throw new IllegalArgumentException("해당 일정에는 이미 예약된 경기장입니다.");
        }
    }

    public MatchResponse getAcceptMatchInfo(Long matchId, AcceptTeamMemberRequest request, String email) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(ErrorCode.MATCH_NOT_FOUND));

        match.updateStatus(MatchStatus.RECRUITMENT_ENDS);

        Team homeTeam = competeTeamRepository.findHomeTeamByMatchId(match.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        Team awayTeam = competeTeamRepository.findAwayTeamByMatchId(match.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        List<TeamMemberSimpleResponse> awayTeamMembers = request.getTeamMemberIdList().stream()
                .flatMap(teamMemberId -> {
                    List<TeamMember> teamMembers = teamMemberRepository.findByIdList(teamMemberId);
                    return teamMembers.stream()
                            .map(teamMember -> {
                                if(user.getNickname().equals(teamMember.getUser().getNickname())){
                                    CompeteTeamMember awayHostTeamMember = CompeteTeamMember.builder()
                                            .nickname(teamMember.getUser().getNickname())
                                            .competeType(CompeteType.AWAY_TEAM_HOST)
                                            .match(match)
                                            .build();
                                    competeTeamMemberRepository.save(awayHostTeamMember);

                                    return getTeamMemberSimpleResponse(teamMember);
                                }
                                CompeteTeamMember awayTeamMember = CompeteTeamMember.builder()
                                        .nickname(teamMember.getUser().getNickname())
                                        .competeType(CompeteType.AWAY_TEAM)
                                        .match(match)
                                        .build();
                                competeTeamMemberRepository.save(awayTeamMember);

                                return getTeamMemberSimpleResponse(teamMember);
                            });
                }).toList();

        AcceptCompete acceptCompete = acceptCompeteRepository.findByMatchId(matchId);
        acceptCompete.setAcceptTeamMember(teamMemberRepository.findTeamMemberByUserId(user.getId()));
        acceptCompete.updateStatus(AcceptStatus.ACCEPT);

        return new MatchResponse().toMatchResponse(match, homeTeam.getTeamName(), awayTeam.getTeamName(), awayTeamMembers);
    }

    public List<MatchResponse> getTeamMatchList(String teamName) {

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        List<Match> matchList = competeTeamRepository.findMatchByTeamId(team.getId());
        return matchList.stream()
                .map(match -> new MatchResponse().toMatchResponse(match))
                .toList();
    }

    private TeamMemberSimpleResponse getTeamMemberSimpleResponse(TeamMember teamMember) {
        URL url = amazonS3Client.getUrl(bucket, "기본이미지.png");
        String userImageUrl = url.toString();
        return new TeamMemberSimpleResponse().toTeamMemberSimpleResponse(teamMember, userImageUrl, teamMember.getRole());
    }
}