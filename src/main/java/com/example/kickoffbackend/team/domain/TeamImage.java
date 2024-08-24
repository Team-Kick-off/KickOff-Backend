package com.example.kickoffbackend.team.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "team_image")
public class TeamImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String storedName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public TeamImage(String originalName, String storedName, Team team) {
        this.originalName = originalName;
        this.storedName = storedName;
        if(team != null){
            changeTeamImage(team);
        }
    }

    private void changeTeamImage(Team team){
        this.team = team;
    }

}
