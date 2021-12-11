package com.tagall.tipsnbills.module.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDto {

    private Long id;

    public ProjectResponseDto(Long id, String name, LocalDateTime start_date, LocalDateTime end_date, String description, String leaderEmail, Long leaderId, List<String> tagNameList) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.description = description;
        this.leaderEmail = leaderEmail;
        this.leaderId = leaderId;
        this.tagNameList = tagNameList;
    }

    private String name;

    private LocalDateTime start_date;

    private LocalDateTime end_date;

    private String description;

    private byte[] image;

    private String leaderEmail;

    private Long leaderId;

    private List<String> tagNameList;

}
