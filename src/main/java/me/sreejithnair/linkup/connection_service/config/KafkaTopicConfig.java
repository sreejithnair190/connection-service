package me.sreejithnair.linkup.connection_service.config;

import me.sreejithnair.linkup.connection_service.constants.Event;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.sreejithnair.linkup.connection_service.constants.Event.CONNECTION_REQUEST_SENT;
import static me.sreejithnair.linkup.connection_service.constants.Event.CONNECTION_REQUEST_ACCEPTED;


@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sendConnectionRequestTopic() {
        return new NewTopic(CONNECTION_REQUEST_SENT, 3, (short) 1);
    }

    @Bean
    public NewTopic acceptConnectionRequestTopic() {
        return new NewTopic(CONNECTION_REQUEST_ACCEPTED, 3, (short) 1);
    }
}
