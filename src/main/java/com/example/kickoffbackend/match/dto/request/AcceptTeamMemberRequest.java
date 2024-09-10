package com.example.kickoffbackend.match.dto.request;

import com.example.kickoffbackend.match.domain.type.Level;
import com.example.kickoffbackend.team.domain.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptTeamMemberRequest {

    private List<Long> teamMemberIdList = new ArrayList<>();

}
