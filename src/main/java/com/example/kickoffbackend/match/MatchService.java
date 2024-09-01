package com.example.kickoffbackend.match;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.kickoffbackend.common.error.ApiException;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.match.domain.Match;
import com.example.kickoffbackend.match.domain.type.MatchStatus;
import com.example.kickoffbackend.match.dto.TeamMemberSimpleInfoResponse;
import com.example.kickoffbackend.match.dto.TeamSimpleInfoResponse;
import com.example.kickoffbackend.match.repository.MatchRepository;
import com.example.kickoffbackend.team.domain.Team;
import com.example.kickoffbackend.team.domain.TeamMember;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true) //⭐
@AllArgsConstructor
@Service
public class MatchService {

    private final AmazonS3Client amazonS3Client;

    @Autowired
    public MatchService(AmazonS3Client amazonS3Client, MatchRepository matchRepository, TeamRepository teamRepository, TeamMemberRepository teamMemberRepository, UserRepository userRepository) {
        this.amazonS3Client = amazonS3Client;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
//        this.competeTeamRepository = competeTeamRepository;
        this.userRepository = userRepository;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
//    private final CompeteTeamRepository competeTeamRepository;
    private final UserRepository userRepository;

    public List<TeamSimpleInfoResponse> findTeamList(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND_ERROR));

        List<Team> teamList = teamMemberRepository.findByUserId(user.getId());
        return teamList.stream()
                .map(team -> new TeamSimpleInfoResponse(team.getTeamName(), amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName()).toString()))
                .toList();

    }

    public List<TeamMemberSimpleInfoResponse> findTeamMemberList(String teamName) {

        Team team = teamRepository.findByName(teamName)
                .orElseThrow(() -> new ApiException(ErrorCode.TEAM_NOT_FOUND));

        String teamImageUrl = "";
        if(!team.getTeamImages().isEmpty()){
            URL url = amazonS3Client.getUrl(bucket, team.getTeamImages().get(0).getStoredName());
            teamImageUrl = url.toString();
        }

        return team.getTeamMembers().stream()
                .map(teamMember -> new TeamMemberSimpleInfoResponse(teamMember.getUser().getNickname()))
                .toList();
    }

}