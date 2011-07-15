package org.jboss.as.console.client;


import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.layout.LHSNavTree;
import org.jboss.as.console.client.layout.LHSNavTreeItem;
import org.jboss.as.console.client.samples.Sample;
import org.jboss.as.console.client.widgets.stack.DisclosureStackPanel;

import java.util.List;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class Navigation {


    private List<Sample> samples;

    public Navigation(List<Sample> samples) {
        this.samples = samples;
    }

    public Widget asWidget() {

        VerticalPanel stack = new VerticalPanel();
        stack.setStyleName("fill-layout");

        DisclosurePanel panel = new DisclosureStackPanel("Widgets").asWidget();

        LHSNavTree tree = new LHSNavTree("widgets");
        for(Sample sample : samples)
        {
            TreeItem item = new LHSNavTreeItem(sample.getName(), "widgets;show="+sample.getId());
            tree.addItem(item);
        }

        panel.setContent(tree);

        stack.add(panel);
        return stack;
    }
}
