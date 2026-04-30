package org.ziranziyuanting.common.commonminio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    /**
     * MinIO endpoint URL (e.g., http://localhost:9000)
     */
    private String endpoint = "http://localhost:9000";
    
    /**
     * Access Key (Username)
     */
    private String accessKey = "minioadmin";
    
    /**
     * Secret Key (Password)
     */
    private String secretKey = "minioadmin";
    
    /**
     * Default bucket name
     */
    private String bucketName = "di-zai-user-manage";
}