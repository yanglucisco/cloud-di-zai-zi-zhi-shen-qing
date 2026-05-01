package org.ziranziyuanting.account.controller;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.common.file.FileUploadService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("file")
public class FileController {
    private final FileUploadService fileUploadService;
    public FileController(FileUploadService fileUploadService) { 
        this.fileUploadService = fileUploadService;
    }
    @PostMapping("/upload")
    public Mono<String> uploadFile(@RequestPart("file") FilePart file) { 
        return fileUploadService.uploadFile(file);
    }
}
