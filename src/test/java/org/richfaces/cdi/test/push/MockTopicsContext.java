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

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.jboss.weld.exceptions.UnsupportedOperationException;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
@RequestScoped
public class MockTopicsContext extends TopicsContext {

    LinkedList<Message> messages = new LinkedList<Message>();

    @Override
    protected Topic createTopic(TopicKey key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void publish(TopicKey key, Object data) throws MessageException {
        this.messages.add(new Message(key, data));
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public Message getLastMessage() {
        return messages.getLast();
    }

    public static class Message {

        public Message(TopicKey topicKey, Object data) {
            super();
            this.topicKey = topicKey;
            this.data = data;
        }

        TopicKey topicKey;
        Object data;

        public TopicKey getTopicKey() {
            return topicKey;
        }

        public Object getData() {
            return data;
        }
    }
}
