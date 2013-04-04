package org.jboss.as.console.client.gin;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import org.jboss.as.console.client.DefaultPlaceManager;
import org.jboss.as.console.client.WidgetPresenter;
import org.jboss.as.console.client.WidgetView;
import org.jboss.as.console.client.layout.MainLayoutPresenter;
import org.jboss.as.console.client.layout.MainLayoutViewImpl;

public class ShowcaseModule extends AbstractPresenterModule {

    protected void configure() {

        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(PlaceManager.class).to(DefaultPlaceManager.class).in(Singleton.class);
        bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        bind(RootPresenter.class).asEagerSingleton();

        // main layout
        bindPresenter(MainLayoutPresenter.class,
                MainLayoutPresenter.MainLayoutView.class,
                MainLayoutViewImpl.class,
                MainLayoutPresenter.MainLayoutProxy.class);

        bindPresenter(WidgetPresenter.class,
                WidgetPresenter.MyView.class,
                WidgetView.class,
                WidgetPresenter.MyProxy.class);
    }

}