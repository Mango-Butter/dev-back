package com.mangoboss.batch.transfer.domain.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.mangoboss.batch.common.util.S3FileManager;
import com.mangoboss.storage.metadata.ContentType;
import com.mangoboss.storage.metadata.S3FileType;
import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PayslipGenerator {
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private final Clock clock;
    private final S3FileManager s3FileManager;

    @Value("${payslip.template-path}")
    private String payslipTemplatePath;

    @Value("${pdf.font-paths}")
    private String fontPaths; // 폰트 경로

    public String renderPayslipHtml(final PayrollEntity payroll) throws Exception {
        ClassPathResource resource = new ClassPathResource(payslipTemplatePath);
        Map<String, String> dataMap = new HashMap<>();

        PayrollAmount payrollAmount = payroll.getPayrollAmount();
        LocalDateTime now = LocalDateTime.now(clock);
        dataMap.put("storeName", payroll.getStaffName());
        dataMap.put("businessNumber", payroll.getBusinessNumber());
        dataMap.put("staffName", payroll.getStaffName());
        dataMap.put("transferDate", payroll.getTransferredAt().format(DATE_FORMATTER));
        dataMap.put("workPeriod", getPeriod(payroll.getMonth()));
        dataMap.put("baseAmount", payrollAmount.getBaseAmount().toString());
        dataMap.put("weeklyAllowance", payrollAmount.getWeeklyAllowance().toString());
        dataMap.put("totalCommutingAllowance", payrollAmount.getTotalCommutingAllowance().toString());
        dataMap.put("totalAmount", payrollAmount.getTotalAmount().toString());
        dataMap.put("withholdingType", payroll.getWithholdingType().getLabel());
        dataMap.put("withholdingTax", payrollAmount.getWithholdingTax().toString());
        dataMap.put("netAmount", payrollAmount.getNetAmount().toString());
        dataMap.put("withdrawalBankName", payroll.getWithdrawalBankcode().getDisplayName());
        dataMap.put("withdrawalAccount", payroll.getWithdrawalAccount());
        dataMap.put("bossName", payroll.getBossName());
        dataMap.put("depositBankName", payroll.getDepositBankCode().getDisplayName());
        dataMap.put("depositAccount", payroll.getDepositAccount());
        dataMap.put("now", now.format(DATE_TIME_FORMATTER));


        try (InputStream inputStream = resource.getInputStream()) {
            String filledHtml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                filledHtml = filledHtml.replace("${" + entry.getKey() + "}", entry.getValue());
            }
            return filledHtml;
        }
    }

    private String getPeriod(final LocalDate month) {
        LocalDate start = month.withDayOfMonth(1);
        LocalDate end = month.withDayOfMonth(month.lengthOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return start.format(formatter) + " - " + end.format(formatter);
    }

    public byte[] generatePdfFromHtml(final String htmlContent) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();

            for (String path : getFontPaths()) {
                ClassPathResource fontResource = new ClassPathResource(path);
                File tempFontFile = File.createTempFile("temp-font", ".ttf");
                tempFontFile.deleteOnExit();

                try (InputStream is = fontResource.getInputStream();
                     FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                    fos.write(is.readAllBytes());
                    fontProvider.addFont(tempFontFile.getAbsolutePath());
                }
            }

            properties.setFontProvider(fontProvider);
            properties.setCharset("UTF-8");

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            HtmlConverter.convertToPdf(htmlContent, pdfDoc, properties);

            return baos.toByteArray();
        }
    }

    private List<String> getFontPaths() {
        return Arrays.stream(fontPaths.split(","))
                .map(String::trim)
                .toList();
    }

    public String savePayslipPdf(final byte[] pdfBytes) {
        String fileKey = s3FileManager.generateFileKey(S3FileType.PAYSLIP, ContentType.PDF.getExtension());
        s3FileManager.upload(pdfBytes, fileKey, ContentType.PDF.getMimeType());
        return fileKey;
    }
}
