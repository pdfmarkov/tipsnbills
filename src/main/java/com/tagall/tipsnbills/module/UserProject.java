package com.tagall.tipsnbills.module;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="userproject")
public class UserProject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "is_leader")
    private boolean isLeader;

    @Column(name = "invitation_time")
    private LocalDateTime invitation_time;

    // TODO: П О Д У М А Й   Н А Д   К А С К Е Й Д Т А Й П . О Л Л ! ! ! ! ! ! Э Т О В А Ж Н О
    // Вы не должны использовать CascadeType.ALL на @ManyToOne ,
    // так как переходы состояний сущностей должны распространяться от родительских сущностей к дочерним,
    // а не наоборот.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    public UserProject(User user, Boolean isAccepted, boolean isLeader) {
        this.user = user;
        this.isAccepted = isAccepted;
        this.isLeader = isLeader;
        this.invitation_time = LocalDateTime.now();
    }

    public UserProject(User user, Boolean isAccepted, boolean isLeader, Project project) {
        this.user = user;
        this.isAccepted = isAccepted;
        this.isLeader = isLeader;
        this.invitation_time = LocalDateTime.now();
        this.project = project;
    }
}
