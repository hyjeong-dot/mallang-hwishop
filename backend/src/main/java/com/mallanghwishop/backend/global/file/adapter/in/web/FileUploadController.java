package com.mallanghwishop.backend.global.file.adapter.in.web;

import com.mallanghwishop.backend.global.file.application.port.in.UploadFileUseCase;
import com.mallanghwishop.backend.global.file.domain.FileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload-file")
@RequiredArgsConstructor
public class FileUploadController {

    private final UploadFileUseCase uploadFileUseCase;

    @PostMapping
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = uploadFileUseCase.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileInfo);
    }
}
