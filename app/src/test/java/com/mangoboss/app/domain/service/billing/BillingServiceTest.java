package com.mangoboss.app.domain.service.billing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.domain.repository.BillingRepository;
import com.mangoboss.app.dto.billing.response.BillingCardInfoResponse;
import com.mangoboss.app.dto.subscription.response.BillingCustomerKeyResponse;
import com.mangoboss.app.external.tosspayment.TossPaymentClient;
import com.mangoboss.storage.billing.BillingEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {

    @InjectMocks
    private BillingService billingService;

    @Mock
    private TossPaymentClient tossPaymentClient;

    @Mock
    private BillingRepository billingRepository;

    private final Long bossId = 1L;

    @Test
    void 고객키가_이미_존재하면_저장하지_않고_반환한다() {
        final String customerKey = "boss-1-" + UUID.randomUUID();
        BillingEntity billing = mock(BillingEntity.class);
        when(billing.getCustomerKey()).thenReturn(customerKey);
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.of(billing));

        BillingCustomerKeyResponse response = billingService.getOrCreateCustomerKey(bossId);

        assertEquals(customerKey, response.customerKey());
        verify(billingRepository, never()).save(any());
    }

    @Test
    void 고객키가_없으면_새로_생성하고_저장한다() {
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.empty());

        BillingCustomerKeyResponse response = billingService.getOrCreateCustomerKey(bossId);

        assertNotNull(response.customerKey());
        verify(billingRepository).save(any());
    }

    @Test
    void 빌링키_발급에_성공하면_정상적으로_등록한다() {
        final String customerKey = "boss-1-" + UUID.randomUUID();
        final String billingKey = "billing-key";
        final String cardDataJson = "{\"issuerCode\":\"123\",\"number\":\"1234-5678-****-****\",\"cardType\":\"신용\",\"ownerType\":\"개인\"}";

        Map<String, Object> mockResponse = Map.of(
                "billingKey", billingKey,
                "card", JsonConverter.fromJson(cardDataJson, new TypeReference<Map<String, Object>>() {})
        );

        BillingEntity billing = mock(BillingEntity.class);

        when(tossPaymentClient.issueBillingKey(eq(customerKey), anyString())).thenReturn(mockResponse);
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.of(billing));

        billingService.issueBillingKey(bossId, customerKey, "auth-key");

        verify(billing).registerBillingKey(eq(billingKey), anyString());
    }

    @Test
    void 빌링정보가_없으면_빌링키_발급시_예외를_던진다() {
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> billingService.issueBillingKey(bossId, "key", "auth"));

        assertEquals(CustomErrorInfo.BILLING_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void 빌링키가_있으면_삭제에_성공한다() {
        BillingEntity billing = mock(BillingEntity.class);
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.of(billing));

        billingService.deleteBillingKey(bossId);

        verify(billingRepository).delete(billing);
    }

    @Test
    void 빌링정보가_없으면_삭제시_예외를_던진다() {
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> billingService.deleteBillingKey(bossId));
    }

    @Test
    void 빌링정보가_존재하면_검증에_성공한다() {
        when(billingRepository.existsByBossId(bossId)).thenReturn(true);
        assertDoesNotThrow(() -> billingService.validateBillingExists(bossId));
    }

    @Test
    void 빌링정보가_없으면_검증시_예외를_던진다() {
        when(billingRepository.existsByBossId(bossId)).thenReturn(false);
        assertThrows(CustomException.class,
                () -> billingService.validateBillingExists(bossId));
    }

    @Test
    void 빌링카드정보가_정상적으로_조회된다() {
        final String billingKey = "billing-key";
        final String cardDataJson = "{\"issuerCode\":\"123\",\"number\":\"1234-5678-****-****\",\"cardType\":\"신용\",\"ownerType\":\"개인\"}";

        BillingEntity billing = mock(BillingEntity.class);
        when(billing.getBillingKey()).thenReturn(billingKey);
        when(billing.getCardData()).thenReturn(cardDataJson);
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.of(billing));

        BillingCardInfoResponse response = billingService.getBillingCardInfo(bossId);

        assertEquals("신용", response.cardType());
        assertEquals("개인", response.ownerType());
        assertEquals("알 수 없음", response.cardCompany());
    }

    @Test
    void 카드정보가_없으면_null을_반환한다() {
        BillingEntity entity = mock(BillingEntity.class);
        when(entity.getBillingKey()).thenReturn(null);
        when(billingRepository.findByBossId(bossId)).thenReturn(Optional.of(entity));

        BillingCardInfoResponse response = billingService.getBillingCardInfo(bossId);

        assertNull(response.cardType());
        assertNull(response.ownerType());
        assertNull(response.cardCompany());
    }
}