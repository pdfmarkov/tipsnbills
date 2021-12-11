package com.tagall.tipsnbills.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "organization",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username")
        })
public class Organization {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{12})")
    @Column(name = "phone_number")
    private String phone_number;

    @NotBlank
    @Column(name = "name_organization")
    private String name_organization;

    @NotBlank
    @Column(name = "login_name")
    private String login_name;

    @NotBlank
    @Column(name = "agreement")
    private String agreement;

    @NotNull
    @Column(name = "state")
    private boolean state;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "userroles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Organization() {}

    public Organization(String username, String password, String login_name, String phone_number, String name_organization, String agreement, boolean state) {
        this.login_name = login_name;
        this.password = password;
        this.phone_number = phone_number;
        this.name_organization = name_organization;
        this.username = username;
        this.agreement = agreement;
        this.state =state;
    }

}
