package com.mallanghwishop.backend.global.file.application.port.in;

import com.mallanghwishop.backend.global.file.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUseCase {
    FileInfo uploadFile(MultipartFile file);
}
