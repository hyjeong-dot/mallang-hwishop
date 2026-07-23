package com.mallanghwishop.backend.auth.application.port.in;

import com.mallanghwishop.backend.auth.application.command.SignupCommand;
import com.mallanghwishop.backend.auth.application.result.SignupResult;

public interface SignupUseCase {
    SignupResult signup(SignupCommand command);
}
