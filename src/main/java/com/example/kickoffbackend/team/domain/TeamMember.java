package com.example.kickoffbackend.team.domain;

import com.example.kickoffbackend.common.BaseEntity;
import com.example.kickoffbackend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_members_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public TeamMember(User user, Team team, Role role){
        this.role = role;
        this.user = user;
        if(team != null){
            changeTeam(team);
        }
    }

    private void changeTeam(Team team){
        this.team = team;
        team.getTeams().add(this);
    }

}
