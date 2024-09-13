package com.example.kickoffbackend.match.controller;

import com.example.kickoffbackend.auth.UserPrincipal;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.match.MatchService;
import com.example.kickoffbackend.match.dto.request.AcceptTeamMemberRequest;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.match.dto.response.MatchResponse;
import com.example.kickoffbackend.match.dto.response.TeamMemberSimpleResponse;
import com.example.kickoffbackend.match.dto.response.TeamSimpleResponse;
import com.example.kickoffbackend.team.dto.response.TeamResponse;
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
    public CustomApi<List<TeamSimpleResponse>> teamList(@AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        String email = userPrincipal.getUsername();

        return CustomApi.OK(matchService.findTeamList(email), email + "님의 팀 조회를 완료했습니다."); // 순서때문에 헤맸잖아...
    }

    @GetMapping("/{teamName}/teamMemberList")
    public CustomApi<List<TeamMemberSimpleResponse>> teamMemberList(@PathVariable("teamName") String teamName) {

        return CustomApi.OK(matchService.findTeamMemberList(teamName), teamName + "팀의 팀원 조회를 완료했습니다.");
    }

    @GetMapping("/{teamName}/search")
    public CustomApi<List<TeamSimpleResponse>> awayTeamList
            (@PathVariable("teamName") String teamName,
            @RequestParam(value = "keyword") String keyword) {

        return CustomApi.OK(matchService.searchAwayTeamList(teamName, keyword), "상대팀 검색 조회를 완료했습니다.");
    }

    @PostMapping
    public CustomApi create(@RequestBody MatchCreateRequest matchCreateRequest) throws IOException {

        return CustomApi.OK(matchService.createMatch(matchCreateRequest), "경기 생성 및 상대팀 참가요청 알림이 전송되었습니다.");
    }

    @GetMapping("/{teamName}")
    public CustomApi getTeamInfo(@PathVariable("teamName") String teamName) {

        return CustomApi.OK(matchService.getTeamInfo(teamName), "팀 조회가 완료되었습니다.");
    }
}
