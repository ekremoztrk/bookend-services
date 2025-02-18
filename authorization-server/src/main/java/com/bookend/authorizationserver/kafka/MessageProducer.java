package com.bookend.authorizationserver.kafka;

import com.bookend.authorizationserver.payload.KafkaUserRegistered;
import com.bookend.authorizationserver.payload.MailRequest;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void sendConfirmationMailRequest(MailRequest mailRequest) {

        LOGGER.info("sending book='{}' to topic='{}'", mailRequest, "confirmation-mail");
        kafkaTemplate.send("89d4u1n3-confirmation-mail", mailRequest);
    }

    public void sendUserInformation(KafkaUserRegistered kafkaUserRegistered) {

        LOGGER.info("sending book='{}' to topic='{}'", kafkaUserRegistered, "user-registered");
        kafkaTemplate.send("89d4u1n3-user-registered", kafkaUserRegistered);
    }

    public void sendResetPasswordMailRequest(MailRequest mailRequest) {

        LOGGER.info("sending mail='{}' to topic='{}'", mailRequest, "resetPassword-mail");
        kafkaTemplate.send("resetPassword-mail", mailRequest);
    }

    public void test() {

        kafkaTemplate.send("resetPassword-mail", "test");
    }


}
