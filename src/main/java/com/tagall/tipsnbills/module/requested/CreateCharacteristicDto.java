package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreateCharacteristicDto {

    @NotNull
    private Long employeeId;

    private Long money;

    private String review;

    private Integer rating;

}
