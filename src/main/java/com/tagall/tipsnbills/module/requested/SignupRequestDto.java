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
    private String phone_number;

    @NotBlank
    private String name_organization;

}
