package com.mallanghwishop.backend.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileCommand {
    private final String username;
    private final String name;
    private final String password;
    private final String email;
    private final String phoneNumber;
    private final String zipcode;
    private final String address;
    private final String detailAddress;
    private final String refundBank;
    private final String refundAccount;
    private final String refundHolder;
}
