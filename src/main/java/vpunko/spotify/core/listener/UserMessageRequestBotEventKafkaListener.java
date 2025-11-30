package vpunko.spotify.core.listener;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import vpunko.spotify.core.dto.UserMessageRequestEvent;
import vpunko.spotify.core.service.MusicEventBotSenderService;

import static vpunko.spotify.core.constant.MetricsConstant.TELEGRAM_BOT_LISTENER_COUNTER;

/**
 * Take a message from music-event-bot
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserMessageRequestBotEventKafkaListener {

    private final MusicEventBotSenderService botSenderService;

    @KafkaListener(topics = "${spring.kafka.in.telegram-bot-user-received-event.topic}")
    @Counted(value = TELEGRAM_BOT_LISTENER_COUNTER)
    void listener(@Payload UserMessageRequestEvent data) {
        log.info("Received message [{}] in topic music-event-core-topic }", data);

        botSenderService.sendMessageToMusicBot(data);
    }
}