package com.mallanghwishop.backend.auth.application.port.in;

import com.mallanghwishop.backend.auth.application.command.UpdateProfileCommand;

public interface UpdateProfileUseCase {
    void updateProfile(UpdateProfileCommand command);
}
