package com.mangoboss.batch.common.util;

import com.mangoboss.storage.metadata.S3FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileManager {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.private-bucket}")
    private String privateBucket;

    @Value("${cloud.aws.s3.kms-key-id}")
    private String kmsKeyId;

    private static final String SSE_ALGORITHM = "aws:kms";


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

    public String generateFileKey(final S3FileType fileType, String extension) {
        return fileType.getFolder() + UUID.randomUUID() + "." + extension;
    }
}
