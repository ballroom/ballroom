package org.jboss.as.console.client.gin;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import org.jboss.as.console.client.WidgetPresenter;
import org.jboss.as.console.client.layout.MainLayoutPresenter;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
@GinModules(ShowcaseModule.class)
public interface ShowcaseUI extends Ginjector {

    PlaceManager getPlaceManager();
    EventBus getEventBus();

    AsyncProvider<MainLayoutPresenter> getMainLayoutPresenter();
    AsyncProvider<WidgetPresenter> getWidgetPresenter();

}
