package org.jboss.as.console.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import org.jboss.as.console.client.samples.Sample;
import org.jboss.as.console.client.samples.TableSample;
import org.jboss.as.console.client.samples.WindowSample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class WidgetView extends ViewImpl implements WidgetPresenter.MyView {

    private WidgetPresenter presenter;
    private TabLayoutPanel content;

    private List<Sample> samples = new ArrayList<Sample>();
    private String selectedId;

    public WidgetView() {

        samples.add(new TableSample());
        samples.add(new WindowSample());
    }

    @Override
    public void selectSample(String sampleId) {
        this.selectedId = sampleId;
        if(content!=null)
            showSample(sampleId);
    }

    @Override
    public Widget asWidget() {

        DockLayoutPanel layout = new DockLayoutPanel(Style.Unit.PX);

        // nav
        Navigation nav = new Navigation(samples);

        // tabs
        content = new TabLayoutPanel(25, Style.Unit.PX);
        content.addStyleName("default-tabpanel");

        showSample(selectedId);

        // assembly
        layout.addWest( nav.asWidget(), 180);
        layout.add(content);

        return layout;
    }

    private void showSample(String id) {

        Sample match = null;

        for(Sample candidate : samples)
        {
            if(id.equals(candidate.getId()))
            {
                match = candidate;
                break;
            }
        }

        if(null==match)
            throw new RuntimeException("No matching sample: "+id);

        content.clear();
        content.add(match.asWidget(), match.getName());
        content.selectTab(0);

    }

    @Override
    public void setPresenter(WidgetPresenter presenter) {
        this.presenter = presenter;
    }


}
