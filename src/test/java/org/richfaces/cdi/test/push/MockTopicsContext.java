package org.richfaces.cdi.test.push;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.jboss.weld.exceptions.UnsupportedOperationException;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;

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
