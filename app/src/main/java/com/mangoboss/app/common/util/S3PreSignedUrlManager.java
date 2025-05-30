package com.mangoboss.app.common.util;

import com.mangoboss.app.common.config.S3Config;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.metadata.S3FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3PreSignedUrlManager {

    @Value("${pre-signed-url.view-expiration-minutes}")
    private int viewExpirationMinutes;

    @Value("${pre-signed-url.download-expiration-minutes}")
    private int downloadExpirationMinutes;

    @Value("${pre-signed-url.upload-expiration-minutes}")
    private int uploadExpirationMinutes;

    private final S3Presigner s3Presigner;
    private final S3Config s3Config;
    private final Clock clock;

    public ViewPreSignedUrlResponse generateViewPreSignedUrl(final String key) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getPrivateBucket())
                .key(key)
                .responseContentDisposition("inline")
                .build();

        final GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(viewExpirationMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        final String url = s3Presigner.presignGetObject(preSignRequest).url().toString();

        return ViewPreSignedUrlResponse.of(url, LocalDateTime.now(clock).plusMinutes(viewExpirationMinutes));
    }

    public DownloadPreSignedUrlResponse generateDownloadPreSignedUrl(final String key) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getPrivateBucket())
                .key(key)
                .responseContentDisposition("attachment")
                .build();

        final GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(downloadExpirationMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        final String url = s3Presigner.presignGetObject(preSignRequest).url().toString();

        return DownloadPreSignedUrlResponse.of(url, LocalDateTime.now(clock).plusMinutes(downloadExpirationMinutes));
    }

    public UploadPreSignedUrlResponse generateUploadPreSignedUrl(final String key, final String contentType) {
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getPublicBucket())
                .key(key)
                .contentType(contentType)
                .build();

        final PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(uploadExpirationMinutes))
                .putObjectRequest(putObjectRequest)
                .build();

        final String uploadUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();
        final String publicUrl = s3Config.getPublicBaseUrl() + key;

        return UploadPreSignedUrlResponse.of(uploadUrl, publicUrl, LocalDateTime.now(clock).plusMinutes(uploadExpirationMinutes));
    }

    public String generateFileKey(final S3FileType fileType, String extension) {
        return fileType.getFolder() + UUID.randomUUID() + "." + extension;
    }
}