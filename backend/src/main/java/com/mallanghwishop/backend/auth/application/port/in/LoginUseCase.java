package com.mallanghwishop.backend.auth.application.port.in;

import com.mallanghwishop.backend.auth.application.command.LoginCommand;
import com.mallanghwishop.backend.auth.application.result.LoginResult;

public interface LoginUseCase {
    LoginResult login(LoginCommand command);
}
