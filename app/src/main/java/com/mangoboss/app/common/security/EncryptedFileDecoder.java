package com.mangoboss.app.common.security;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class EncryptedFileDecoder {

    private final AesEncryptor aesEncryptor;

    /**
     * 암호화된 base64 파일을 복호화 및 파싱하여 구조화된 객체로 반환
     */
    public DecodedFile decode(final String encryptedBase64) {
        try {
            final String decryptedBase64 = aesEncryptor.decrypt(encryptedBase64);
            final String mimeType = extractMimeType(decryptedBase64);
            final String base64Body = extractBase64Body(decryptedBase64);
            final byte[] fileBytes = Base64.getDecoder().decode(base64Body);
            return new DecodedFile(mimeType, fileBytes);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.FILE_DECRYPTION_FAILED);
        }
    }

    /**
     * 복호화된 문자열에서 MIME 타입 추출
     */
    public String extractMimeType(final String decryptedBase64) {
        final String prefix = decryptedBase64.substring(0, decryptedBase64.indexOf(";"));
        return prefix.replace("data:", "");
    }

    /**
     * base64 본문만 추출 ("data:image/png;base64,...")
     */
    public String extractBase64Body(final String decryptedBase64) {
        return decryptedBase64.substring(decryptedBase64.indexOf(",") + 1);
    }

    public record DecodedFile(
            String mimeType,
            byte[] fileBytes
    ) {}

}
