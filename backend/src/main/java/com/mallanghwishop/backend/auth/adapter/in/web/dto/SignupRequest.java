package com.mallanghwishop.backend.auth.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mallanghwishop.backend.auth.application.command.SignupCommand;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
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

    public SignupCommand toCommand() {
        return new SignupCommand(username, password, name, email, phoneNumber, zipcode, address, detailAddress, refundBank, refundAccount, refundHolder);
    }
}
