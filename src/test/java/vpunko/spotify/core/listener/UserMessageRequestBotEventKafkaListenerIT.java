package vpunko.spotify.core.listener;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import vpunko.spotify.core.dto.UserMessageRequestEvent;
import vpunko.spotify.core.service.MusicEventBotSenderService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = "music-event-core-topic",
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
@TestPropertySource(properties = {
        "spring.kafka.in.telegram-bot-user-received-event.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}",
})
class UserMessageRequestBotEventKafkaListenerIT {

    private static final String TOPIC = "music-event-core-topic";

    @Autowired
    private KafkaTemplate<String, UserMessageRequestEvent> kafkaTemplate;

    @MockitoBean
    private MusicEventBotSenderService botSenderService;

    @Test
    void shouldConsumeEventAndCallService() {
        // given
        UserMessageRequestEvent event =
                new UserMessageRequestEvent(12345L, "hello from test");

        // when — send message to Kafka
        kafkaTemplate.send(TOPIC, event);

        // then — verify listener consumed it
        verify(botSenderService, timeout(10000))
                .sendMessageToMusicBot(event);
    }

    @TestConfiguration
    static class KafkaTestConfig {

        @Value("${spring.embedded.kafka.brokers}")
        private String bootstrapServers;

        @Bean
        public ProducerFactory<String, UserMessageRequestEvent> producerFactoryTest() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            return new DefaultKafkaProducerFactory<>(props);
        }

        @Bean
        public KafkaTemplate<String, UserMessageRequestEvent> kafkaTemplateTest(ProducerFactory<String, UserMessageRequestEvent> producerFactoryTest) {
            return new KafkaTemplate<>(producerFactoryTest);
        }
    }
}