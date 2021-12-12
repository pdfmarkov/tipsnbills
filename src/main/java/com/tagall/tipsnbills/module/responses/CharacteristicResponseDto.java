package com.tagall.tipsnbills.module.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicResponseDto {

    private Long id;

    private String review;

    private Integer rating;

    private Long money;

    private LocalDateTime time;

}
