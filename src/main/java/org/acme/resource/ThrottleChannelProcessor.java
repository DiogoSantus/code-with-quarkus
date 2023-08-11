package org.acme.resource;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ThrottleChannelProcessor {

    @Inject
    @Channel("throttle-channel")
    Emitter<String> emitter;

    @Incoming("throttle-channel")
    public void processThrottleChannel(String data) {
        // Implement my throttling logic here before emitting the data
        emitter.send(data);
    }
}
