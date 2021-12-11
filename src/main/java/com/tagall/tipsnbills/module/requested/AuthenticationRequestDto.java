package com.tagall.tipsnbills.module.requested;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String login;
    private String password;
    private String code;
}
