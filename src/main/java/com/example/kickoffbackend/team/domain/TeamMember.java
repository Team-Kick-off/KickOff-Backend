package com.example.kickoffbackend.team.domain;

import com.example.kickoffbackend.common.BaseEntity;
import com.example.kickoffbackend.match.domain.AcceptCompete;
import com.example.kickoffbackend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teamMembers")
public class TeamMember extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_members_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "acceptTeamMember")
    private List<AcceptCompete> acceptCompete;

    private String teamRequestContent;

    private boolean teamRequestCheck;

    @Builder
    public TeamMember(User user, Team team, Role role, String teamRequestContent){
        this.role = role;
        this.user = user;
        this.teamRequestContent = teamRequestContent;
        this.teamRequestCheck = false;
        if(team != null){
            changeTeam(team);
        }
    }

    private void changeTeam(Team team){
        this.team = team;
        team.getTeamMembers().add(this);
    }

}
