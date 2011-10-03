package org.richfaces.cdi.push;

import org.richfaces.application.push.TopicKey;

public class PushMessage implements Cloneable {

    private TopicKey topic;
    private Object payload;

    public PushMessage(TopicKey topic) {
        this.topic = topic;
    }

    public PushMessage(TopicKey topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public TopicKey getTopic() {
        return topic;
    }

    public Object getPayload() {
        return payload;
    }

    public PushMessage topic(TopicKey topic) {
        PushMessage clone = cloneSafely();
        clone.topic = topic;
        return clone;
    }

    public PushMessage payload(Object payload) {
        PushMessage clone = cloneSafely();
        clone.payload = payload;
        return clone;
    }

    private PushMessage cloneSafely() {
        try {
            return (PushMessage) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(PushMessage.class.getSimpleName() + " does support cloning");
        }
    }
}
