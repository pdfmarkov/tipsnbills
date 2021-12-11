package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class IdRequestDto {

    @NotNull
    private Long id;
}
