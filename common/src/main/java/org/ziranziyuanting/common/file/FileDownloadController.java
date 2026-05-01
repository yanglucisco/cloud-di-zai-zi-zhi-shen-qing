package org.ziranziyuanting.common.file;

import org.springframework.core.io.InputStreamResource;
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
import org.ziranziyuanting.common.commonminio.MinioService;

import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileDownloadController {

    private final MinioService minioService;

    public FileDownloadController(MinioService minioService, FileUploadService fileUploadService) {
        this.minioService = minioService;
    }
    /**
     * Download a file from MinIO by object name.
     *
     * @param objectName The name of the object in MinIO (e.g., uuid_filename.ext)
     * @return ResponseEntity with the file content
     */
    @GetMapping("/download/{objectName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String objectName) {
        try {
            // Define the bucket name (should match the one used in upload)
            String bucketName = "di-zai-user-manage";

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
