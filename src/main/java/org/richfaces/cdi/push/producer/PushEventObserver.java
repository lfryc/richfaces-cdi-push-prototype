package org.richfaces.cdi.push.producer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.PushMessage;

public class PushEventObserver {

    @Inject
    TopicsContext topicsContext;

    public void observePushEvent(@Observes PushMessage pushEvent) throws MessageException {
        topicsContext.publish(pushEvent.getTopic(), pushEvent.getPayload());
    }
}
