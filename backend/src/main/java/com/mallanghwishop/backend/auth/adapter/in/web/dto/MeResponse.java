package com.mallanghwishop.backend.auth.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {
    private UUID memberId;
    private String username;
    private String name;
    private String role;
    private String email;
    private String phoneNumber;
    
    private String zipcode;
    private String address;
    private String detailAddress;
    
    private String refundBank;
    private String refundAccount;
    private String refundHolder;
    
    private int currentPoint;
}
