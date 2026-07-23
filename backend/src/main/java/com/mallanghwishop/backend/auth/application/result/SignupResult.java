package com.mallanghwishop.backend.auth.application.result;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class SignupResult {
    private final UUID memberId;
    private final String username;
}
