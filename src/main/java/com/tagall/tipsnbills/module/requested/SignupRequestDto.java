package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.Set;

@Data
public class SignupRequestDto {
    @NotBlank
    @Email
    private String username;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Pattern(regexp = "(^$|[0-9]{12})")
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
}
