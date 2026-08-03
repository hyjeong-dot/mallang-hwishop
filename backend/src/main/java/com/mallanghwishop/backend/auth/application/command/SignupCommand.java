package com.mallanghwishop.backend.auth.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupCommand {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;

    private String zipcode;
    private String address;
    private String detailAddress;
    
    private String refundBank;
    private String refundAccount;
    private String refundHolder;
}
