package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TagWithIdRequestDto {
    @NotNull
    private Long id;

    @NotBlank
    private String tagName;
}
