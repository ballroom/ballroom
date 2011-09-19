package org.jboss.ballroom.client.spi;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanFactory;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 * @author Heiko Braun
 * @author David Bosschaert
 * @date 7/14/11
 */
public class NoopFramework implements Framework {

    private static EventBus bus = new SimpleEventBus();

    public EventBus getEventBus() {
        return bus;
    }

    public PlaceManager getPlaceManager() {
        throw new RuntimeException("No PlaceManager available!");
    }

    public AutoBeanFactory getBeanFactory() {
        // An empty AutoBeanFactory
        return new AutoBeanFactory() {
            @Override
            public <T, U extends T> AutoBean<T> create(Class<T> clazz, U delegate) {
                return null;
            }

            @Override
            public <T> AutoBean<T> create(Class<T> clazz) {
                return null;
            }
        };
    }
}
