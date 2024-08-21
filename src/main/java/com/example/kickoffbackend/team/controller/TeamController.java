package com.example.kickoffbackend.team.controller;

import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public CustomApi create(@ModelAttribute TeamCreateRequest teamCreateRequest, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        System.out.println("teamCreateRequest " + teamCreateRequest);
        if(userDetails == null){
            return CustomApi.ERROR(ErrorCode.valueOf("로그인이 필요합니다."));
        }

        String email = userDetails.getUsername();

        return CustomApi.OK("팀 생성이 완료되었습니다.", teamService.create(teamCreateRequest, email));
    }
}
