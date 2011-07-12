package org.jboss.as.console.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
@GinModules(CoreUIModule.class)
public interface CoreUI extends Ginjector {

    PlaceManager getPlaceManager();
    EventBus getEventBus();
    ProxyFailureHandler getProxyFailureHandler();

}
