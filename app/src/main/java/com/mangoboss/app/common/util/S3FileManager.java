package com.mangoboss.app.common.util;

import com.mangoboss.app.common.constant.ContentType;
import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.*;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileManager {
    private static final String SSE_ALGORITHM = "aws:kms";

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final Clock clock;

    @Getter
    @Value("${cloud.aws.s3.public-base-url}")
    private String publicBaseUrl;

    @Value("${cloud.aws.s3.kms-key-id}")
    private String kmsKeyId;

    @Value("${pre-signed-url.view-expiration-minutes}")
    private int viewExpirationMinutes;

    @Value("${pre-signed-url.download-expiration-minutes}")
    private int downloadExpirationMinutes;

    @Value("${pre-signed-url.upload-expiration-minutes}")
    private int uploadExpirationMinutes;

    @Getter
    @Value("${cloud.aws.s3.private-bucket}")
    private String privateBucket;

    @Getter
    @Value("${cloud.aws.s3.public-bucket}")
    private String publicBucket;


    // 파일 업로드: byte[] 기반
    public void upload(final byte[] fileData, final String key, final String contentType) {
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(privateBucket)
                .key(key)
                .contentType(contentType)
                .serverSideEncryption(SSE_ALGORITHM)
                .ssekmsKeyId(kmsKeyId)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(fileData));
    }

    public void upload(String key, byte[] fileBytes, ContentType contentType) {
        final PutObjectRequest request = PutObjectRequest.builder()
                .bucket(privateBucket)
                .key(key)
                .contentType(contentType.getMimeType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(fileBytes));
    }

    public void deleteFile(final String key) {
        try {
            final DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(privateBucket)
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
                            .bucket(privateBucket)
                            .key(key)
                            .build()
            );
            return objectBytes.asByteArray();
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.S3_OBJECT_FETCH_FAILED);
        }
    }

    // 조회용 presingedUrl (inline)
    public ViewPreSignedUrlResponse generateViewPreSignedUrl(final String key) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(privateBucket)
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

    // 다운로드용 presingedUrl
    public DownloadPreSignedUrlResponse generateDownloadPreSignedUrl(final String key) {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(privateBucket)
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

    // 업로드용 presingedUrl
    public UploadPreSignedUrlResponse generateUploadPreSignedUrl(final String key, final String contentType) {
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(publicBucket)
                .key(key)
                .contentType(contentType)
                .build();

        final PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(uploadExpirationMinutes))
                .putObjectRequest(putObjectRequest)
                .build();

        final String uploadUrl = s3Presigner.presignPutObject(preSignRequest).url().toString();
        final String publicUrl = publicBaseUrl + key;

        return UploadPreSignedUrlResponse.of(uploadUrl, publicUrl, LocalDateTime.now(clock).plusMinutes(uploadExpirationMinutes));
    }

    public void deleteFileFromTaskBucket(final String key) {
        try {
            final DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(publicBucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteRequest);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.FILE_DELETE_FAILED);
        }
    }

    public String generateFileKey(final S3FileType fileType, String extension) {
        return fileType.getFolder() + UUID.randomUUID() + "." + extension;
    }

    public String getContentTypeFromS3(final String key) {
        try {
            final HeadObjectRequest request = HeadObjectRequest.builder()
                    .bucket(privateBucket)
                    .key(key)
                    .build();

            final HeadObjectResponse response = s3Client.headObject(request);
            return response.contentType();
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.S3_OBJECT_FETCH_FAILED);
        }
    }

    public String extractKeyFromUrl(final String url, final String baseUrl) {
        if (!url.startsWith(baseUrl)) {
            throw new CustomException(CustomErrorInfo.INVALID_S3_URL);
        }
        return url.substring(baseUrl.length());
    }
}