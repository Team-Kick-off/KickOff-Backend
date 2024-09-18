package com.example.kickoffbackend.team.controller;

import com.example.kickoffbackend.auth.UserPrincipal;
import com.example.kickoffbackend.common.CustomApi;
import com.example.kickoffbackend.common.error.ErrorCode;
import com.example.kickoffbackend.team.dto.response.TeamSimpleResponse;
import com.example.kickoffbackend.team.domain.Gender;
import com.example.kickoffbackend.team.domain.RecruitmentStatus;
import com.example.kickoffbackend.team.dto.request.TeamCreateRequest;
import com.example.kickoffbackend.team.dto.request.TeamRegisterRequest;
import com.example.kickoffbackend.team.dto.response.TeamFilterResponse;
import com.example.kickoffbackend.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/filter")
    public CustomApi<Page<TeamFilterResponse>> findByTeamFilter(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "gender", required = false) Gender gender,
            @RequestParam(value = "stauts", required = false, defaultValue = "OPEN") RecruitmentStatus status,
            @PageableDefault(size = 10) Pageable pageable){

        return CustomApi.OK(teamService.findTeamFilter(address, gender, status, pageable));
    }

    @GetMapping("/check-duplicate")
    public CustomApi<Boolean> checkDuplicateTeamName(@RequestParam("teamName") String teamName) {
        boolean isDuplicate = teamService.isTeamNameDuplicate(teamName);
        return CustomApi.OK(isDuplicate, isDuplicate ? "이미 존재하는 팀 이름입니다." : "사용 가능한 팀 이름입니다.");
    }

    /**
     * Match 관련 조회 Controller
     * Description : 경기_생성, 경기_수락, 경기_운영관리 관련
     **/

    @GetMapping("/userTeam") // 경기 생성_로그인 유저의 팀들 조회
    public CustomApi<List<TeamSimpleResponse>> getTeamByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        String email = userPrincipal.getUsername();

        return CustomApi.OK(teamService.findTeamByUser(email), email + "님의 팀 조회를 완료했습니다.");
    }

    @GetMapping("/{teamName}/teamMembers") // 경기 생성_선택한 팀의 팀원들 조회
    public CustomApi getTeamMemberList(@PathVariable("teamName") String teamName) {

        return CustomApi.OK(teamService.findTeamMemberList(teamName), teamName + "팀의 팀원 조회를 완료했습니다.");
    }

    @GetMapping("/{teamName}/search") // 경기 생성_상대팀 검색 조회
    public CustomApi<List<TeamSimpleResponse>> searchAwayTeam
            (@PathVariable("teamName") String teamName) {

        return CustomApi.OK(teamService.searchAwayTeamList(teamName), "상대팀 검색 조회를 완료했습니다.");
    }

    @GetMapping("/{matchId}/awayTeamMembers")
    public CustomApi getTeamSimpleInfo(@PathVariable("matchId") Long matchId) {

        return CustomApi.OK(teamService.findAwayTeamMemberList(matchId), "Away팀 및 팀원 조회가 완료되었습니다.");
    }
}
