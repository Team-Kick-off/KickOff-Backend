package com.example.kickoffbackend.match.controller;

import com.example.kickoffbackend.auth.UserPrincipal;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.match.MatchService;
import com.example.kickoffbackend.match.dto.request.AcceptTeamMemberRequest;
import com.example.kickoffbackend.match.dto.request.MatchCreateRequest;
import com.example.kickoffbackend.match.dto.response.MatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public CustomApi create(@RequestBody MatchCreateRequest matchCreateRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {

        String email = userPrincipal.getUsername();

        return CustomApi.OK(matchService.createMatch(matchCreateRequest, email), "경기 생성 및 상대팀 참가요청 알림이 전송되었습니다.");
    }

    @PostMapping("/{matchId}/awayTeam/accept")
    public CustomApi acceptMatch(@PathVariable("matchId") Long matchId, @RequestBody AcceptTeamMemberRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String email = userPrincipal.getUsername();

        return CustomApi.OK(matchService.getAcceptMatchInfo(matchId, request, email), "수락 요청 경기의 조회가 완료되었습니다.");
    }


}
