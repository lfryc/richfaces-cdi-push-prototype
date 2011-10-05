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

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.Push;
import org.richfaces.util.FastJoiner;

import com.google.common.base.Function;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public abstract class PublishingTestCase {

    private static JavaArchive createJavaArchive() {
        JavaArchive javaArchive = ShrinkWrap
                .create(JavaArchive.class, PublishingTestCase.class.getSimpleName() + ".jar")
                // Push implementation package
                .addPackage(Push.class.getPackage())
                // test-specific components
                .addClasses(PublishingTestCase.class, MockTopicsContext.class, MessageException.class,
                        TestingDependencyRegistrationExtension.class)
                // RichFaces dependencies
                .addClasses(TopicsContext.class, Topic.class, TopicKey.class, Function.class, FastJoiner.class)
                .addAsManifestResource("testing-extension", "services/javax.enterprise.inject.spi.Extension");

        System.out.println();
        javaArchive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();

        return javaArchive;
    }

    public static WebArchive createBaseTestArchive() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, PublishingTestCase.class.getSimpleName() + ".war")
                .addAsLibrary(createJavaArchive()).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        System.out.println();
        webArchive.writeTo(System.out, Formatters.VERBOSE);
        System.out.println();

        return webArchive;
    }
}
