package org.richfaces.cdi.push.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.richfaces.application.push.TopicsContext;

public class TopicsContextProducer {
    
    @Produces
    @ApplicationScoped
    public TopicsContext getTopicsContext() {
        return TopicsContext.lookup();
    }
}
