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

import javax.enterprise.util.AnnotationLiteral;

import org.junit.Before;
import org.junit.Test;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.TopicKeyResolver;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class TestTopicKeyResolver {

    private TopicKeyResolver resolver;

    @Before
    public void setup() {
        resolver = new TopicKeyResolver();
    }

    @Test
    public void testSimpleTopicAddress() {
        TopicKey expected = new TopicKey("simple");
        TopicKey actual = resolver.resolveTopicKey(new PushAnnotation("simple"));
        assertEquals(expected, actual);
    }

    @Test
    public void testComposedTopicAddress() {
        TopicKey expected = new TopicKey("composed@address");
        TopicKey actual = resolver.resolveTopicKey(new PushAnnotation("composed@address"));
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtopic() {
        TopicKey expected = new TopicKey("subtopic@topic");
        TopicKey actual = resolver.resolveTopicKey(new PushAnnotation("topic", "subtopic"));
        assertEquals(expected, actual);
    }

    private class PushAnnotation extends AnnotationLiteral<Push> implements Push {
        private static final long serialVersionUID = -1L;
        private String topic;
        private String subtopic;

        public PushAnnotation(String topic) {
            this(topic, "");
        }

        public PushAnnotation(String topic, String subtopic) {
            this.topic = topic;
            this.subtopic = subtopic;
        }

        public String value() {
            return topic;
        }

        public String subtopic() {
            return subtopic;
        }
    }
}
