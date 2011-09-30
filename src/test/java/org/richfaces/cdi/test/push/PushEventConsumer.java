package org.richfaces.cdi.test.push;

import java.util.LinkedList;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

@RequestScoped
public class PushEventConsumer {

    private LinkedList<Object> observedEvents = new LinkedList<Object>();

    public void observesPushEvent(@Observes String event) {
        observedEvents.add(event);
    }

    public LinkedList<Object> getObservedEvents() {
        return observedEvents;
    }
}
