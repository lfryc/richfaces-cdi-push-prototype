package org.richfaces.cdi.test.push;

import static org.junit.Assert.assertEquals;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushMessage;
import org.richfaces.cdi.push.producer.PushEventObserver;
import org.richfaces.cdi.push.producer.PushEventProducer;
import org.richfaces.cdi.push.producer.TopicKeyProducer;

@RunWith(Arquillian.class)
public class TestPublishing {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Push.class.getPackage())
                .addClass(PushEventProducer.class).addClass(PushEventObserver.class).addClass(MockTopicsContext.class)
                .addClass(TopicKeyProducer.class)
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension");
    }

    private static final String TOPIC_NAME_1 = "sampleTopic1";

    @Inject
    Event<PushMessage> event;

    @Inject
    @Push(TOPIC_NAME_1)
    PushMessage message;

    @Inject
    @Push(TOPIC_NAME_1)
    TopicKey topic;

    @Inject
    private MockTopicsContext topicsContext;

    @Test
    public void testTopicPublishingWithInjectedPushEvent() throws MessageException {
        event.fire(message.payload("test"));

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        event.fire(message.payload("anotherTest"));

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }

    @Test
    public void testTopicPublishingWithInjectedTopicKey() throws MessageException {
        event.fire(new PushMessage(topic, "test"));

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        event.fire(new PushMessage(topic, "anotherTest"));

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }
}
