package com.example.kickoffbackend.team.controller;

import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.dto.request.TeamRegisterRequest;
import com.example.kickoffbackend.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping(consumes = "multipart/form-data")
    public CustomApi create(@ModelAttribute TeamCreateRequest teamCreateRequest, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if(userDetails == null){
            return CustomApi.ERROR(ErrorCode.USER_NOT_FOUND_ERROR);
        }

        String email = userDetails.getUsername();

        return CustomApi.OK("팀 생성이 완료되었습니다.", teamService.create(teamCreateRequest, email));
    }


    @GetMapping("/{teamName}")
    public CustomApi getTeamInfo(@PathVariable("teamName") String teamName){
        return CustomApi.OK(teamService.getTeamInfo(teamName), "팀 조회가 완료되었습니다.");
    }

    @PostMapping("/{teamName}/members")
    public CustomApi registerTeamMember(@PathVariable("teamName") String teamName,
                                        @RequestBody TeamRegisterRequest teamRegisterRequest,
                                        @AuthenticationPrincipal UserDetails userDetails){

        if(userDetails == null){
            return CustomApi.ERROR(ErrorCode.USER_NOT_FOUND_ERROR);
        }

        String email = userDetails.getUsername();
        String teamRequestContent = teamRegisterRequest.getTeamRequestContent();

        return CustomApi.OK(teamService.teamRegister(teamName, email, teamRequestContent));
    }

}
