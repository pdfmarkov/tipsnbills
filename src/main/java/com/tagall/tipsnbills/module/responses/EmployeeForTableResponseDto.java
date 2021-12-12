package com.tagall.tipsnbills.module.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForTableResponseDto {
    private String name;
    private String surname;
    private Double rating; //avg
    private String email;
    private String card;
}
