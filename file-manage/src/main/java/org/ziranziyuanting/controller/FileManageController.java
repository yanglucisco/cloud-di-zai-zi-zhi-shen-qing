package org.ziranziyuanting.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.ziranziyuanting.common.commonminio.MinioProperties;
import org.ziranziyuanting.common.commonminio.MinioService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("file")
public class FileManageController {
    private final MinioService minioService;
    private final MinioProperties minioProperties;
    public FileManageController(MinioService minioService, MinioProperties minioProperties) {
        this.minioService = minioService;
        this.minioProperties = minioProperties;
    }
    @PostMapping("/upload")
    public Mono<String> uploadFile(@RequestPart("file") FilePart file) { 
        String bucketName = "di-zai-user-manage";
        String originalFilename = file.filename();
        UUID uuid = UUID.randomUUID();
        String objectName = uuid.toString() + "_" + originalFilename;
        return DataBufferUtils.join(file.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .flatMap(bytes -> {
                    try {
                        // Convert byte array to InputStream for your existing service
                        java.io.InputStream inputStream = new java.io.ByteArrayInputStream(bytes);
                        String contentType = file.headers().getContentType().toString();
                        minioService.uploadFile(bucketName, objectName, inputStream, contentType);
                        String fileUrl = String.format("%s", objectName);
                        return Mono.just(fileUrl);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
    @GetMapping("/download/{objectName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectName) {
        try {
            // Define the bucket name (should match the one used in upload)
            String bucketName = minioProperties.getBucketName();

            // Retrieve the file input stream from MinIO
            InputStream inputStream = minioService.downloadFile(bucketName, objectName);

            // Determine content type (optional: you might want to store/retrieve metadata)
            // For now, using application/octet-stream as a generic binary type
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + 
                URLEncoder.encode(objectName, StandardCharsets.UTF_8.toString()) + "\"");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            // Return the response entity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .body(new InputStreamResource(inputStream));

        } catch (Exception e) {
            // Handle exceptions (e.g., file not found)
            return ResponseEntity.internalServerError().build();
        }
    }
}
