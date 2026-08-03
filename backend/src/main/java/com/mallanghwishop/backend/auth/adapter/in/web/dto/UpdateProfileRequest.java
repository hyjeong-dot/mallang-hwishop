package com.mallanghwishop.backend.auth.adapter.in.web.dto;

import com.mallanghwishop.backend.auth.application.command.UpdateProfileCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String detailAddress;
    private String refundBank;
    private String refundAccount;
    private String refundHolder;

    public UpdateProfileCommand toCommand(String username) {
        return UpdateProfileCommand.builder()
                .username(username)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .zipcode(this.zipcode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .refundBank(this.refundBank)
                .refundAccount(this.refundAccount)
                .refundHolder(this.refundHolder)
                .build();
    }
}
