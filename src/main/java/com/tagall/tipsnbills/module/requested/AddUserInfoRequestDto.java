package com.tagall.tipsnbills.module.requested;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserInfoRequestDto {

    private String name;

    private String surname;

    private LocalDateTime birth_date;

    private String description;

}
