package com.mallanghwishop.backend.global.file.application.port.out;

import com.mallanghwishop.backend.global.file.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface SaveFilePort {
    FileInfo save(MultipartFile file);
}
