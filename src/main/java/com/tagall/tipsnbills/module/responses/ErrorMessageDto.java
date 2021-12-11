package com.tagall.tipsnbills.module.responses;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessageDto {

    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public ErrorMessageDto(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

}
