package org.richfaces.cdi.push.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushMessage;

public class PushEventProducer {

    @Produces
    public PushMessage createPushEvent(InjectionPoint injectionPoint) {
        TopicKey topic = TopicKeyProducer.getTopicKey(injectionPoint.getAnnotated().getAnnotation(Push.class));
        return new PushMessage(topic);
    }
}
