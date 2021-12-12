package com.tagall.tipsnbills.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Pattern(regexp="(^$|[0-9]{11})")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{16})")
    @Column(name = "card_number")
    private String cardNumber;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Characteristic> characteristics = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subsidiary_id", referencedColumnName = "id")
    private Subsidiary subsidiary;

    public Employee() {}

    public Employee(String name,String surname,String patronymic,
                    String phoneNumber,String email,String cardNumber) {
        this.name = name;
        this.surname =surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email =email;
        this.cardNumber=cardNumber;
    }
}
