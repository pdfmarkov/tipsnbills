package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateEmployeeDto {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String patronymic;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{11})")
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp="(^$|[0-9]{16})")
    private String cardNumber;
}
