package org.richfaces.cdi.test.push;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.application.push.TopicKey;
import org.richfaces.cdi.push.Push;
import org.richfaces.cdi.push.PushMessage;
import org.richfaces.cdi.push.producer.PushEventProducer;

@RunWith(Arquillian.class)
public class TestPushEventProducer {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackage(Push.class.getPackage())
                .addClass(PushEventProducer.class)
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension");
    }

    private static final String SIMPLE_NAME = "topic";
    private static final String COMPLEX_NAME = "subtopic@topic";
    private static final String SUBTOPIC_NAME = "subtopic";

    @Inject
    Instance<PushMessage> pushEventInstance;

    @Inject
    @Push(SIMPLE_NAME)
    PushMessage pushEventWithSimpleTopicName;

    @Inject
    @Push(COMPLEX_NAME)
    PushMessage pushEventWithComplexTopicName;

    @Inject
    @Push(value = SIMPLE_NAME, subtopic = SUBTOPIC_NAME)
    PushMessage pushEventWithComposedTopicName;

    @Test(expected = IllegalStateException.class)
    public void topicKeyWithoutPushAnnotationShouldThrowException() {
        pushEventInstance.get();
    }

    @Test
    public void topicKeyShouldSupportSimpleTopicName() {
        assertThat(pushEventWithSimpleTopicName.getTopic(), equalTo(new TopicKey(SIMPLE_NAME)));
    }

    @Test
    public void topicKeyShouldSupportComplexTopicName() {
        assertThat(pushEventWithComplexTopicName.getTopic(), equalTo(new TopicKey(COMPLEX_NAME)));
    }

    @Test
    public void topicKeyShouldSupportComposedTopicName() {
        assertThat(pushEventWithComposedTopicName.getTopic(), equalTo(new TopicKey(SIMPLE_NAME, SUBTOPIC_NAME)));
    }
}
