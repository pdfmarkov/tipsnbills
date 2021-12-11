package com.tagall.tipsnbills.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

    @NotBlank
    @Column(name = "patronymic")
    private String patronymic;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{12})")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{16})")
    @Column(name = "cardNumber")
    private String cardNumber;

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
