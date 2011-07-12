package org.jboss.as.console.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class WidgetView extends ViewImpl implements WidgetPresenter.MyView {

    private WidgetPresenter presenter;

    @Override
    public Widget asWidget() {

        DockLayoutPanel layout = new DockLayoutPanel(Style.Unit.PX);

        // LHS nav



        return layout;
    }

    @Override
    public void setPresenter(WidgetPresenter presenter) {
        this.presenter = presenter;
    }
}
