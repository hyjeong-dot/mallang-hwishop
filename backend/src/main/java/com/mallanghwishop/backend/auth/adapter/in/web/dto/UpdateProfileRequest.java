package com.mallanghwishop.backend.auth.adapter.in.web.dto;

import com.mallanghwishop.backend.auth.application.command.UpdateProfileCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String nickname;
    private String password; // optional
    private String email;
    private String phoneNumber;

    public UpdateProfileCommand toCommand(String username) {
        return UpdateProfileCommand.builder()
                .username(username)
                .nickname(this.nickname)
                .password(this.password)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
