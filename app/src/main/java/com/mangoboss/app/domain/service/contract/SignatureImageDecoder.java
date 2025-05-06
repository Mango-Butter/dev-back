package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.AesEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SignatureImageDecoder {
    private final AesEncryptor aesEncryptor;

    public byte[] decodeSignatureImageBytes(final String signatureData) {
        try {
            // 1. 복호화 (AES + CBC + IV)
            final String decryptedBase64 = aesEncryptor.decrypt(signatureData);

            // 2. base64 prefix 제거
            final String base64DataOnly = decryptedBase64.substring(decryptedBase64.indexOf(",") + 1);

            // 3. base64 → byte[]
            return Base64.getDecoder().decode(base64DataOnly);

        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.SIGNATURE_UPLOAD_FAILED);
        }
    }
}
