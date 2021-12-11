package com.tagall.tipsnbills.module;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@Entity
@Table(name="project")
public class Project {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime start_date;

    @Column(name = "end_date")
    private LocalDateTime end_date;

    @Column(name = "description")
    private String description;

    @Column(name = "connect")
    private String connect;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<UserProject> userProjectList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "projecttag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tagList;

    public Project(String name, LocalDateTime start_date, LocalDateTime end_date, String description, String connect, UserProject... userProjects) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.connect = connect;

        for (UserProject userProject : userProjects)
            userProject.setProject(this);
        this.userProjectList = Stream.of(userProjects).collect(Collectors.toList());
    }
}
