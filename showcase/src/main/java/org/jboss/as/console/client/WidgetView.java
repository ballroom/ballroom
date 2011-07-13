package org.jboss.as.console.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import org.jboss.as.console.client.samples.TableSample;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class WidgetView extends ViewImpl implements WidgetPresenter.MyView {

    private WidgetPresenter presenter;
    private TabLayoutPanel content;

    @Override
    public Widget asWidget() {

        DockLayoutPanel layout = new DockLayoutPanel(Style.Unit.PX);

        // nav
        Navigation nav = new Navigation();

        // tabs
        content = new TabLayoutPanel(25, Style.Unit.PX);
        content.addStyleName("default-tabpanel");

        TableSample tableSample = new TableSample();
        content.add(tableSample.asWidget(), tableSample.getName());


        content.selectTab(0);

        // assembly
        layout.addWest( nav.asWidget(), 180);
        layout.add(content);

        return layout;
    }

    @Override
    public void setPresenter(WidgetPresenter presenter) {
        this.presenter = presenter;
    }


}
