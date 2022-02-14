package org.acme.rabbitmq.producer.base;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.quarkiverse.rabbitmqclient.NamedRabbitMQClient;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

public abstract class BaseProducer<T> {

    protected String exchange = "%s-exchange";
    protected String queue = "%s-queue";

    @Inject
    @NamedRabbitMQClient("foo")
    protected RabbitMQClient fooClient;

    @Inject
    @NamedRabbitMQClient("bar")
    protected RabbitMQClient barClient;

    protected Channel channel;
    public abstract void send(String tenantId, T object);

    /**
     * Set up the queues for the producer.
     */
    protected void setupQueues(final String tenantId, final String producerName) {
        try {
            // create a connection
            Connection connection = null;
            switch (tenantId) {
                case "foo":
                    connection = fooClient.connect();
                    break;
                case "bar":
                    connection = barClient.connect();
                    break;
                default:
                    break;
            }

            // create a channel
            channel = connection.createChannel();
            // declare exchanges and queues

            exchange = String.format(exchange, producerName);
            queue = String.format(queue, producerName);

            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true);
            channel.queueDeclare(queue, true, false, false, null);
            channel.queueBind(queue, exchange, "");
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}