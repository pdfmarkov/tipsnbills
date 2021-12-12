package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateSubsidiaryDto {

    @NotBlank
    private String subsidiary;
}
