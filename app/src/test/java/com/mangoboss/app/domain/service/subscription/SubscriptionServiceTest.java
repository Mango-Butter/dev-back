package com.mangoboss.app.domain.service.subscription;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.SubscriptionOrderRepository;
import com.mangoboss.app.domain.repository.SubscriptionRepository;
import com.mangoboss.app.dto.subscription.response.SubscriptionOrderResponse;
import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import com.mangoboss.storage.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionOrderRepository subscriptionOrderRepository;

    @Test
    void 구독이_있으면_삭제하고_새로_생성한다() {
        // given
        UserEntity boss = mock(UserEntity.class);
        when(boss.getId()).thenReturn(1L);
        SubscriptionEntity oldSubscription = mock(SubscriptionEntity.class);
        when(subscriptionRepository.findByBossId(1L)).thenReturn(Optional.of(oldSubscription));

        // when
        subscriptionService.createOrReplaceSubscription(boss, PlanType.PREMIUM);

        // then
        verify(subscriptionRepository).delete(oldSubscription);
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
        verify(boss).addSubscription(any(SubscriptionEntity.class));
    }

    @Test
    void 구독이_없으면_삭제없이_바로_생성된다() {
        // given
        UserEntity boss = mock(UserEntity.class);
        when(boss.getId()).thenReturn(2L);
        when(subscriptionRepository.findByBossId(2L)).thenReturn(Optional.empty());

        // when
        subscriptionService.createOrReplaceSubscription(boss, PlanType.PREMIUM);

        // then
        verify(subscriptionRepository, never()).delete(any());
        verify(subscriptionRepository).save(any(SubscriptionEntity.class));
        verify(boss).addSubscription(any(SubscriptionEntity.class));
    }

    @Test
    void 구독이_있으면_반환한다() {
        // given
        SubscriptionEntity subscription = mock(SubscriptionEntity.class);
        when(subscriptionRepository.findByBossId(1L)).thenReturn(Optional.of(subscription));

        // when
        SubscriptionEntity result = subscriptionService.getSubscription(1L);

        // then
        assertThat(result).isEqualTo(subscription);
    }

    @Test
    void 구독이_없으면_null을_반환한다() {
        // given
        when(subscriptionRepository.findByBossId(1L)).thenReturn(Optional.empty());

        // when
        SubscriptionEntity result = subscriptionService.getSubscription(1L);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 구독삭제시_존재하지_않으면_예외를_던진다() {
        // given
        when(subscriptionRepository.findByBossId(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscriptionService.deleteSubscription(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SUBSCRIPTION_NOT_FOUND.getMessage());
    }

    @Test
    void 구독삭제시_존재하면_삭제한다() {
        // given
        SubscriptionEntity subscription = mock(SubscriptionEntity.class);
        when(subscriptionRepository.findByBossId(1L)).thenReturn(Optional.of(subscription));

        // when
        subscriptionService.deleteSubscription(1L);

        // then
        verify(subscriptionRepository).delete(subscription);
    }

    @Test
    void 주문내역을_반환한다() {
        // given
        SubscriptionOrderEntity order1 = mock(SubscriptionOrderEntity.class);
        SubscriptionOrderEntity order2 = mock(SubscriptionOrderEntity.class);
        when(subscriptionOrderRepository.findByBossIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(order1, order2));

        SubscriptionOrderResponse response1 = mock(SubscriptionOrderResponse.class);
        SubscriptionOrderResponse response2 = mock(SubscriptionOrderResponse.class);

        try (MockedStatic<SubscriptionOrderResponse> mockStatic = mockStatic(SubscriptionOrderResponse.class)) {
            mockStatic.when(() -> SubscriptionOrderResponse.fromEntity(order1)).thenReturn(response1);
            mockStatic.when(() -> SubscriptionOrderResponse.fromEntity(order2)).thenReturn(response2);

            // when
            List<SubscriptionOrderResponse> result = subscriptionService.getOrderHistory(1L);

            // then
            assertThat(result).containsExactlyInAnyOrder(response1, response2);
        }
    }
}