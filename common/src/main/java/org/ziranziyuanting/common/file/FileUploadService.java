package org.ziranziyuanting.common.file;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestPart;

import reactor.core.publisher.Mono;

public interface FileUploadService { 
    Mono<String> uploadFile(@RequestPart("file") FilePart file);
}