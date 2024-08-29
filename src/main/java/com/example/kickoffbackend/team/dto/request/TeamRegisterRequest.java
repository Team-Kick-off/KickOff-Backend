package com.example.kickoffbackend.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamRegisterRequest {

    private String teamRequestContent;

    public TeamRegisterRequest(String teamRequestContent){
        this.teamRequestContent = teamRequestContent;
    }

}
