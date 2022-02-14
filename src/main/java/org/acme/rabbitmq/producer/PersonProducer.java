package org.acme.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.rabbitmq.model.Person;
import org.acme.rabbitmq.producer.base.BaseProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class PersonProducer extends BaseProducer<Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonProducer.class);

    private static final String PRODUCER_NAME = "person";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Send a message to the queue containing the person converted to json
     * @param person the person to send to the queue as json
     */
    @Override
    public void send(final String tenantId, final Person person) {
        try {
            final String message = mapper.writeValueAsString(person);
            setupQueues(tenantId, PRODUCER_NAME);
            channel.basicPublish(exchange, "", null, message.getBytes(StandardCharsets.UTF_8));
            log.info("Sent message: {}", message);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
