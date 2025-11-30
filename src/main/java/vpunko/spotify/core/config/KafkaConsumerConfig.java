package vpunko.spotify.core.config;


import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import vpunko.spotify.core.dto.UserMessageRequestEvent;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.in.telegram-bot-user-received-event.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.in.telegram-bot-user-received-event.consumer.groupId}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, UserMessageRequestEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserMessageRequestEvent.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "vpunko.spotify.core.dto");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserMessageRequestEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, UserMessageRequestEvent> consumerFactory, MeterRegistry meterRegistry) {

        ConcurrentKafkaListenerContainerFactory<String, UserMessageRequestEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setMicrometerEnabled(true);
        consumerFactory.addListener(new MicrometerConsumerListener<>(meterRegistry));
        return factory;
    }
}