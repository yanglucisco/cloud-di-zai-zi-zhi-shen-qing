package org.ziranziyuanting.common.file;

import java.util.UUID;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.ziranziyuanting.common.commonminio.MinioProperties;
import org.ziranziyuanting.common.commonminio.MinioService;

import reactor.core.publisher.Mono;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final MinioService minioService;
    private final MinioProperties minioProperties;
    public FileUploadServiceImpl(MinioService minioService, MinioProperties minioProperties) {
        this.minioService = minioService;
        this.minioProperties = minioProperties;
    }
    public Mono<String> uploadFile(@RequestPart("file") FilePart file) { // Use @RequestPart and FilePart
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
                        String fileUrl = String.format("%s/%s/%s", minioProperties.getEndpoint(), bucketName, objectName);
                        return Mono.just(fileUrl);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
}
