package com.example.kickoffbackend.match.controller;

import com.example.kickoffbackend.auth.UserPrincipal;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.match.MatchService;
import com.example.kickoffbackend.match.dto.TeamMemberSimpleInfoResponse;
import com.example.kickoffbackend.match.dto.TeamSimpleInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/teamList")
    public CustomApi<List<TeamSimpleInfoResponse>> teamList(@AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        String email = userPrincipal.getUsername();

        return CustomApi.OK(matchService.findTeamList(email), email + "님의 팀 조회를 완료했습니다."); // 순서때문에 헤맸잖아...
    }

    @GetMapping
    public CustomApi<List<TeamMemberSimpleInfoResponse>> teamMemberList(@PathVariable("teamName") String teamName) {
        matchService.findTeamMemberList(teamName);
        return CustomApi.OK(teamName + "팀의 팀원 조회를 완료했습니다.");
    }
}
