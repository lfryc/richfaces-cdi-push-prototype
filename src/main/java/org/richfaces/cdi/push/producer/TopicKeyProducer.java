package org.richfaces.cdi.push.producer;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;

public class TopicKeyProducer {

    @Produces
    public TopicKey createTopicKey(InjectionPoint injectionPoint) {
        return getTopicKey(injectionPoint.getAnnotated().getAnnotation(Push.class));
    }

    static TopicKey getTopicKey(Push push) {
        if (push == null) {
            throw new IllegalStateException(Push.class.getSimpleName()
                    + " annotation needs to be defined for TopicKey injection");
        }

        if ("".equals(push.subtopic())) {
            return new TopicKey(push.value());
        } else {
            return new TopicKey(push.value(), push.subtopic());
        }
    }
}
