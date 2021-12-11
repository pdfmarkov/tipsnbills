package com.tagall.tipsnbills.module.requested;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class InvitationRequestDto {

    @NotNull
    private Long projectId;

    @NotNull
    private Long userProjectId;

    private boolean isAccepted;

    private String username;
}
