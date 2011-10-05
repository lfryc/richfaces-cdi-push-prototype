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
import static org.junit.Assert.assertNotNull;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.test.push.qualified.CustomQualifier;
import org.richfaces.cdi.test.push.qualified.QualifiedPushEventObserver;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@RunWith(Arquillian.class)
public class TestPublishingWithQualifiers {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Push.class.getPackage())
                .addClass(MockTopicsContext.class).addPackage(CustomQualifier.class.getPackage())
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension");
    }

    private static final String TOPIC_NAME = "topic";

    @Inject
    @CustomQualifier
    @Push(TOPIC_NAME)
    Event<String> event;

    @Inject
    MockTopicsContext topicsContext;

    @Inject
    QualifiedPushEventObserver observer;

    @Test
    public void testQualifiedPushEventObserving() {
        event.fire("test");
        assertNotNull(observer.getLastEvent());
        assertEquals("test", observer.getLastEvent());
    }

    @Test
    public void testQualifiedPushEventPublishing() {
        event.fire("test");
        assertEquals(1, topicsContext.getMessages().size());
        assertEquals(new TopicKey(TOPIC_NAME), topicsContext.getLastMessage().getTopicKey());
        assertEquals("test", topicsContext.getLastMessage().getData());
    }
}
