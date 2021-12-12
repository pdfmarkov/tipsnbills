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
    @Pattern(regexp="(^$|[0-9]{11})")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @NotBlank
    @Column(name = "nameOrganization")
    private String nameOrganization;

    @NotBlank
    @Column(name = "loginName")
    private String loginName;

    @Column(name = "agreement")
    private String agreement;

    @NotNull
    @Column(name = "state")
    private boolean state;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private Set<Subsidiary> subsidiary = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "userroles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Organization() {}

    public Organization(String username, String password, String loginName, String phoneNumber, String nameOrganization, String agreement, boolean state) {
        this.loginName = loginName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nameOrganization = nameOrganization;
        this.username = username;
        this.agreement = agreement;
        this.state =state;
    }

}
