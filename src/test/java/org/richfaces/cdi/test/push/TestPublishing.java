/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdi.test.push;

import static org.junit.Assert.assertEquals;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@RunWith(Arquillian.class)
public class TestPublishing {

    @Deployment
    public static WebArchive createTestArchive() {
        return PublishingTestCase.createBaseTestArchive();
    }

    private static final String TOPIC_NAME_1 = "sampleTopic1";
    private static final String TOPIC_NAME_2 = "sampleTopic2";

    @Inject
    @Push(topic = TOPIC_NAME_1)
    Event<String> event1;

    @Inject
    @Push(topic = TOPIC_NAME_2)
    Event<String> event2;

    @Inject
    MockTopicsContext topicsContext;

    @Test
    public void testTopicPublishing() throws MessageException {
        event1.fire("test");

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        event1.fire("anotherTest");

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }

    @Test
    public void testTwoTopicsPublishing() throws MessageException {
        event1.fire("test");

        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_1), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());

        event2.fire("anotherTest");

        assertEquals(2, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME_2), topicsContext.getLastMessage().getTopicKey());
        assertEquals("anotherTest", topicsContext.getLastMessage().getData());
    }
}
