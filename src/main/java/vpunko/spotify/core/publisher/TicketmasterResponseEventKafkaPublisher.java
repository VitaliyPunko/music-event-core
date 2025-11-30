package vpunko.spotify.core.publisher;

import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vpunko.spotify.core.dto.TicketmasterResponseEvent;

import static vpunko.spotify.core.constant.MetricsConstant.TICKET_MASTER_RESPONSE_EVENT_PUBLISHER_COUNTER;

/**
 * Producer for sending a ticket master message to music-bot service via Kafka
 */
@Slf4j
@Component
public class TicketmasterResponseEventKafkaPublisher {

    private final String topicName;
    private final KafkaTemplate<String, TicketmasterResponseEvent> kafkaTemplate;

    public TicketmasterResponseEventKafkaPublisher(
            KafkaTemplate<String, TicketmasterResponseEvent> kafkaTemplate,
            @Value("${spring.kafka.out.ticket-master-response-event.topic}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Counted(value = TICKET_MASTER_RESPONSE_EVENT_PUBLISHER_COUNTER)
    public void sendMessage(TicketmasterResponseEvent message, long chatId) {
        log.info("Sending message to music-bot service : {}", message);

        kafkaTemplate.send(topicName, String.valueOf(chatId), message);
    }
}