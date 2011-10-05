package org.richfaces.cdi.push;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.richfaces.cdi.push.producer.TopicsContextProducer;

public class PushCDIDependencyRegistrationExtension implements Extension {

    /**
     * Registers all necessary beans required by this extension.
     */
    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager beanManager) {
        bbd.addAnnotatedType(beanManager.createAnnotatedType(Push.class));
        bbd.addAnnotatedType(beanManager.createAnnotatedType(TopicKeyResolver.class));
        bbd.addAnnotatedType(beanManager.createAnnotatedType(TopicsContextProducer.class));
    }
}
