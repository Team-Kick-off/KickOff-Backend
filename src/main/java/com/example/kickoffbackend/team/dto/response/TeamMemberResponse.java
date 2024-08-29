package com.example.kickoffbackend.team.dto.response;

import com.example.kickoffbackend.team.domain.Role;
import lombok.Data;


@Data
public class TeamMemberResponse {

    private String username;

    private Role role;

    public TeamMemberResponse(String username, Role role){
        this.username = username;
        this.role = role;
    }
}
