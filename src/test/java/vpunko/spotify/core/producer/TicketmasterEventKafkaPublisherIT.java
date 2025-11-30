package vpunko.spotify.core.producer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import vpunko.spotify.core.TestKafkaConsumersUtils;
import vpunko.spotify.core.dto.TicketMasterResponseEvent;
import vpunko.spotify.core.dto.TicketmasterEvent;
import vpunko.spotify.core.publisher.TicketmasterResponseEventKafkaPublisher;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = "ticket-master-response-topic"
)
@TestPropertySource(properties = {
        "spring.kafka.out.ticket-master-response-event.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
})
class TicketmasterEventKafkaPublisherIT {

    private static final String TOPIC = "ticket-master-response-topic";

    @Autowired
    private TicketmasterResponseEventKafkaPublisher publisher;
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void shouldPublishEventToKafkaTopic() {
        // given
        Consumer<String, TicketMasterResponseEvent> consumer =
                TestKafkaConsumersUtils.create(
                        embeddedKafka,
                        TOPIC,
                        TicketMasterResponseEvent.class
                );

        TicketMasterResponseEvent event = new TicketMasterResponseEvent();
        event.setEvents(createEvents());
        event.setChatId(12345L);

        // when
        publisher.sendMessage(event);

        // then: читаем одно сообщение из топика и проверяем
        ConsumerRecords<String, TicketMasterResponseEvent> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        TicketMasterResponseEvent consumed = records.iterator().next().value();

        assertThat(records.count()).isEqualTo(1);
        assertThat(consumed.getEvents())
                .usingRecursiveComparison()
                .isEqualTo(event.getEvents());
    }


    private List<TicketmasterEvent> createEvents() {
        TicketmasterEvent event1 = new TicketmasterEvent();
        event1.setName("Test group");
        event1.setPrice(15.15);

        TicketmasterEvent event2 = new TicketmasterEvent();
        event2.setName("Test group2");
        event2.setPrice(30.1);

        return List.of(event1, event2);
    }
}
