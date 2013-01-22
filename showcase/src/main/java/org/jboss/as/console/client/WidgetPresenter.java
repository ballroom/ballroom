package org.jboss.as.console.client;

import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import org.jboss.as.console.client.layout.MainLayoutPresenter;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class WidgetPresenter extends Presenter<WidgetPresenter.MyView, WidgetPresenter.MyProxy> {

    private final PlaceManager placeManager;

    @ProxyCodeSplit
    @NameToken(NameTokens.widgets)
    public interface MyProxy extends Proxy<WidgetPresenter>, Place {
    }

    public interface MyView extends View {
        void setPresenter(WidgetPresenter presenter);
        void selectSample(String sampleId);
    }

    @Inject
    public WidgetPresenter(EventBus eventBus, MyView view, MyProxy proxy,
                           PlaceManager placeManager) {
        super(eventBus, view, proxy);

        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        String sampleId = request.getParameter("show", "tables");
        getView().selectSample(sampleId);
    }

    @Override
    protected void revealInParent() {
         RevealContentEvent.fire(this, MainLayoutPresenter.TYPE_MainContent, this);
    }
}
