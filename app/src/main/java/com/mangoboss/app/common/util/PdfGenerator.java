package com.mangoboss.app.common.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfGenerator {

    @Value("${pdf.font-path}")
    private String fontPath; // 폰트 경로

    public byte[] generatePdfFromHtml(final String htmlContent) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();

            ClassPathResource fontResource = new ClassPathResource(fontPath);
            log.info("[PDF] fontPath: {}", fontPath);
            log.info("[PDF] fontResource.exists(): {}", fontResource.exists());

            File tempFontFile = File.createTempFile("temp-font", ".ttf");
            tempFontFile.deleteOnExit();
            try (InputStream is = fontResource.getInputStream();
                 FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                fos.write(is.readAllBytes());
            }

            fontProvider.addFont(tempFontFile.getAbsolutePath());
            properties.setFontProvider(fontProvider);
            properties.setCharset("UTF-8");

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            HtmlConverter.convertToPdf(htmlContent, pdfDoc, properties);

            return baos.toByteArray();
        } catch (Exception e) {
            log.error("[PDF] PDF 생성 실패", e);
            throw new CustomException(CustomErrorInfo.PDF_GENERATION_FAILED);
        }
    }
}
