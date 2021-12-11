package com.tagall.tipsnbills.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username")
        })
public class User{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{12})")
    @Column(name = "phone_number")
    private String phone_number;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "surname")
    private String surname;

    @NotBlank
    @Column(name = "patronymic")
    private String patronymic;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{16})")
    @Column(name = "card_number")
    private String card_number;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "userroles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String email, String phone_number,String name,String surname,String patronymic,String card_number) {
        this.email = email;
        this.phone_number = phone_number;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.card_number =card_number;
    }

}
