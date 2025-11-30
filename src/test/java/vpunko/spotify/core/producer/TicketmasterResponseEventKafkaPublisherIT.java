package vpunko.spotify.core.producer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import vpunko.spotify.core.TestKafkaConsumersUtils;
import vpunko.spotify.core.dto.TicketmasterResponseEvent;
import vpunko.spotify.core.publisher.TicketmasterResponseEventKafkaPublisher;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = "ticket-master-response-topic"
)
@TestPropertySource(properties = {
        "spring.kafka.out.ticket-master-response-event.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
})
class TicketmasterResponseEventKafkaPublisherIT {

    private static final String TOPIC = "ticket-master-response-topic";

    @Autowired
    private TicketmasterResponseEventKafkaPublisher publisher;
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void shouldPublishEventToKafkaTopic() {
        // given
        Consumer<String, TicketmasterResponseEvent> consumer =
                TestKafkaConsumersUtils.create(
                        embeddedKafka,
                        TOPIC,
                        TicketmasterResponseEvent.class
                );

        TicketmasterResponseEvent event = new TicketmasterResponseEvent();
        event.setName("Test group");
        event.setPrice(15.15);
        long chatId = 12345L;

        // when
        publisher.sendMessage(event, chatId);

        // then: читаем одно сообщение из топика и проверяем
        ConsumerRecord<String, TicketmasterResponseEvent> record =
                KafkaTestUtils.getSingleRecord(consumer, TOPIC);

        assertThat(record.value()).isEqualTo(event);
    }
}
