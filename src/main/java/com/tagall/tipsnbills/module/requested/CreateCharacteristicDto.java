package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCharacteristicDto {

    private Long money;

    private String review;

    private Integer rating;

    private LocalDateTime time;

}
