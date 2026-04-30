package org.ziranziyuanting.common.commonminio;

import java.io.InputStream;

public interface MinioService {
    /**
     * Upload a file to MinIO
     *
     * @param bucketName Bucket name
     * @param objectName Object name (file path in bucket)
     * @param inputStream Input stream of the file
     * @param contentType Content type of the file
     */
    void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType);

    /**
     * Download a file from MinIO
     *
     * @param bucketName Bucket name
     * @param objectName Object name
     * @return InputStream of the file
     */
    InputStream downloadFile(String bucketName, String objectName);

    /**
     * Delete a file from MinIO
     *
     * @param bucketName Bucket name
     * @param objectName Object name
     */
    void deleteFile(String bucketName, String objectName);

    /**
     * Check if a bucket exists, create if not
     *
     * @param bucketName Bucket name
     */
    void ensureBucketExists(String bucketName);
    
    /**
     * Get presigned URL for downloading
     *
     * @param bucketName Bucket name
     * @param objectName Object name
     * @param expiry Expiry time in seconds
     * @return Presigned URL
     */
    String getPresignedUrl(String bucketName, String objectName, int expiry);
}