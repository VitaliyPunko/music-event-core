package vpunko.spotify.core.publisher;

import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import vpunko.spotify.core.dto.TicketMasterResponseEvent;

import static vpunko.spotify.core.constant.MetricsConstant.TICKET_MASTER_RESPONSE_EVENT_PUBLISHER_COUNTER;

/**
 * Producer for sending a ticket master message to music-bot service via Kafka
 */
@Slf4j
@Component
public class TicketmasterResponseEventKafkaPublisher {

    private final String topicName;
    private final KafkaTemplate<String, TicketMasterResponseEvent> kafkaTemplate;

    public TicketmasterResponseEventKafkaPublisher(
            KafkaTemplate<String, TicketMasterResponseEvent> kafkaTemplate,
            @Value("${spring.kafka.out.ticket-master-response-event.topic}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Counted(value = TICKET_MASTER_RESPONSE_EVENT_PUBLISHER_COUNTER)
    public void sendMessage(TicketMasterResponseEvent message) {
        log.info("Sending message to music-bot service : {}", message);

        kafkaTemplate.send(topicName, String.valueOf(message.getChatId()), message);
    }
}