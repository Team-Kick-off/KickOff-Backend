package com.example.kickoffbackend.team.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_members_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public TeamMember(Team team, Role role){
        this.role = role;
        if(team != null){
            changeTeam(team);
        }
    }

    private void changeTeam(Team team){
        this.team = team;
        team.getTeams().add(this);}

}
