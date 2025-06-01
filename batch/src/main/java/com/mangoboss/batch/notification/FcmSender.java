package com.mangoboss.batch.notification;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mangoboss.storage.notification.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class FcmSender {

    public void send(List<NotificationEntity> notifications) {
        final List<Message> messages = new ArrayList<>();

        for (NotificationEntity notification : notifications) {
            String targetToken = notification.getTargetToken();

            if (targetToken == null || targetToken.isBlank()) {
                notification.markFailure();
                continue;
            }

            messages.add(buildMessage(notification, targetToken));
        }

        if (messages.isEmpty()) return;

        try {
            ApiFuture<BatchResponse> responseFuture = FirebaseMessaging.getInstance().sendEachAsync(messages);
            BatchResponse response = responseFuture.get();

            for (int i = 0; i < response.getResponses().size(); i++) {
                final var sendResponse = response.getResponses().get(i);
                final NotificationEntity notification = notifications.get(i);

                if (sendResponse.isSuccessful()) {
                    notification.markSuccess();
                } else {
                    notification.markFailure();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            for (NotificationEntity notification : notifications) {
                notification.markFailure();
            }
        }
    }

    private Message buildMessage(NotificationEntity notificationEntity, String targetToken) {
        return Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(notificationEntity.getTitle())
                        .setBody(notificationEntity.getContent())
                        .setImage(notificationEntity.getImageUrl())
                        .build())
                .putData("clickUrl", notificationEntity.getClickUrl())
                .build();
    }
}