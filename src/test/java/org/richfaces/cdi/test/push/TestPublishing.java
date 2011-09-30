package org.richfaces.cdi.test.push;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushEvent;
import org.richfaces.cdi.push.producer.PushEventProducer;
import org.richfaces.cdi.test.push.MockTopicsContextProducer.MockTopicsContext;

@RunWith(Arquillian.class)
public class TestPublishing {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Push.class.getPackage())
                .addClass(PushEventProducer.class).addClass(MockTopicsContextProducer.class)
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension");
    }

    private static final String TOPIC_NAME_1 = "sampleTopic1";
    private static final String TOPIC_NAME_2 = "sampleTopic2";

    @Inject
    @Push(TOPIC_NAME_1)
    PushEvent<String> pushEvent1;

    @Inject
    @Push(TOPIC_NAME_2)
    PushEvent<String> pushEvent2;

    @Inject
    private MockTopicsContext topicsContext;

    @Test
    public void testTopicPublishing() throws MessageException {
        pushEvent1.fire("test");

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        pushEvent1.fire("anotherTest");

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }

    @Test
    public void testTwoTopicsPublishing() throws MessageException {
        pushEvent1.fire("test");

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        pushEvent2.fire("anotherTest");

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_2), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }
}
