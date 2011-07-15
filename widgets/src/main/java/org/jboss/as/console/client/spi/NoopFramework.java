package org.jboss.as.console.client.spi;

import com.google.gwt.autobean.shared.AutoBeanFactory;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.jboss.as.console.client.spi.Framework;

/**
 * @author Heiko Braun
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
        throw new RuntimeException("No AutoBean factory available!");
    }
}
