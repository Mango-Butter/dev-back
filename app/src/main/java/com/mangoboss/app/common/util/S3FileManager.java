package com.mangoboss.app.common.util;

import com.mangoboss.app.common.config.S3Config;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.storage.metadata.S3FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileManager {
    private static final String SSE_ALGORITHM = "aws:kms";

    private final S3Client s3Client;
    private final S3Config s3Config;

    // 파일 업로드: byte[] 기반
    public void upload(final byte[] fileData, final String key, final String contentType) {
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3Config.getPrivateBucket())
                .key(key)
                .contentType(contentType)
                .serverSideEncryption(SSE_ALGORITHM)
                .ssekmsKeyId(s3Config.getKmsKeyId())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(fileData));
    }

    public void deleteFileFromPrivateBucket(final String key) {
        try {
            final DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Config.getPrivateBucket())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.FILE_DELETE_FAILED);
        }
    }

    public void deleteFileFromPublicBucket(final String key) {
        try {
            final DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(s3Config.getPublicBucket())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.FILE_DELETE_FAILED);
        }
    }

    // 서명 파일 base64로 가져옴
    public String fetchAsBase64(final String key, final String contentType) {
        final byte[] data = fetchAsBytes(key);
        final String base64 = Base64.getEncoder().encodeToString(data);
        return "data:" + contentType + ";base64," + base64;
    }

    // s3에서 서명 파일 가져옴
    public byte[] fetchAsBytes(final String key) {
        try {
            final ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(s3Config.getPrivateBucket())
                            .key(key)
                            .build()
            );
            return objectBytes.asByteArray();
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.S3_OBJECT_FETCH_FAILED);
        }
    }

    public String generateFileKey(final S3FileType fileType, String extension) {
        return fileType.getFolder() + UUID.randomUUID() + "." + extension;
    }

    public String extractKeyFromPublicUrl(final String url) {
        final String baseUrl = s3Config.getPublicBaseUrl();

        if (!url.startsWith(baseUrl)) {
            throw new CustomException(CustomErrorInfo.INVALID_S3_URL);
        }
        return url.substring(baseUrl.length());
    }
}