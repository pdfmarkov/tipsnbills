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
    @Pattern(regexp = "(^$|[0-9]{11})")
    @Column(name = "phone_number")
    private String phone_number;

    @NotBlank
    @Column(name = "name_organization")
    private String name_organization;

}
