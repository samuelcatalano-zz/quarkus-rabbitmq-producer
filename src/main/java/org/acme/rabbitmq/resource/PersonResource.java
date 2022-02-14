package org.acme.rabbitmq.resource;

import org.acme.rabbitmq.exception.ApiException;
import org.acme.rabbitmq.model.Person;
import org.acme.rabbitmq.producer.PersonProducer;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/person")
public class PersonResource {

    @Inject
    PersonProducer producer;

    @POST
    @Path("{tenantId}/request")
    @Produces(MediaType.APPLICATION_JSON)
    public Person produce(final @PathParam("tenantId") String tenantId, final Person person) throws ApiException {
        try {
            producer.send(tenantId, person);
            return person;
        } catch (final Exception e) {
            throw new ApiException(e);
        }
    }
}
