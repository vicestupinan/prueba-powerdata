package co.com.vmestupinan.events;

import co.com.vmestupinan.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.jackson.JsonCloudEventData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String SOME_EVENT_NAME = "event.stats.validated";
    private final DomainEventBus domainEventBus;
    private final ObjectMapper om;

    @Override
    public Mono<Void> emit(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{SOME_EVENT_NAME, event.toString()});
        CloudEvent eventCloudEvent = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("http://stats-service"))
                .withType(SOME_EVENT_NAME)
                .withTime(OffsetDateTime.now())
                .withData("application/json", JsonCloudEventData.wrap(om.valueToTree(event)))
                .build();

         return from(domainEventBus.emit(eventCloudEvent));
    }
}
