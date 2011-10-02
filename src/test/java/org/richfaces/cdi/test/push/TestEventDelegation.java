package org.richfaces.cdi.test.push;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.MessageException;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushEvent;
import org.richfaces.cdi.push.producer.PushEventProducer;

@RunWith(Arquillian.class)
public class TestEventDelegation {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Push.class.getPackage())
                .addClass(PushEventProducer.class).addClass(MockTopicsContextProducer.class).addClass(PushEventConsumer.class)
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension");
    }

    private static final String TOPIC_NAME = "sampleTopic1";

    @Inject
    @Push(TOPIC_NAME)
    PushEvent<String> pushEvent;

    @Inject
    PushEventConsumer consumer;

    @Test
    public void testEventDelegating() throws MessageException {
        pushEvent.fire("test");
        assertEquals(1, consumer.getObservedEvents().size());
        assertEquals("test", consumer.getObservedEvents().getLast());
        
        pushEvent.fire("anotherTest");
        assertEquals(2, consumer.getObservedEvents().size());
        assertEquals("anotherTest", consumer.getObservedEvents().getLast());
    }
}
