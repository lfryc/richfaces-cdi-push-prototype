package org.richfaces.cdi.push.producer;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushEvent;

public class PushEventProducer {

    @Inject
    TopicsContext topicsContext;

    @Inject
    Event<Object> delegateEvent;

    @Produces
    public PushEvent getPushEvent(final InjectionPoint injectionPoint) {

        final Push pushTarget = injectionPoint.getAnnotated().getAnnotation(Push.class);

        if (pushTarget == null) {
            throw new IllegalStateException("@Inject PushEvent<?> needs to @Push qualifier at injection point: "
                    + injectionPoint);
        }

        return new PushEvent() {

            private TopicKey getTopicKey() {
                if ("".equals(pushTarget.subtopic())) {
                    return new TopicKey(pushTarget.value());
                } else {
                    return new TopicKey(pushTarget.value(), pushTarget.subtopic());
                }
            }

            public void fire(Object event) {
                TopicKey topicKey = getTopicKey();
                try {
                    delegate(event);
                    topicsContext.publish(topicKey, event);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }

            private void delegate(Object event) throws Exception {
                Class<?> clazz = event.getClass();
                Event<Object> selectedEvent = (Event<Object>) delegateEvent.select(clazz);
                selectedEvent.fire(clazz.cast(event));
            }

            public PushEvent select(Annotation... qualifiers) {
                // TODO Auto-generated method stub
                return null;
            }

            public PushEvent select(Class subtype, Annotation... qualifiers) {
                // TODO Auto-generated method stub
                return null;
            }

            public PushEvent select(TypeLiteral subtype, Annotation... qualifiers) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }
}
