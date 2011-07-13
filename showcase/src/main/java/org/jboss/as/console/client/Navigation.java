package org.jboss.as.console.client;


import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.widgets.DisclosureStackHeader;
import org.jboss.as.console.client.widgets.LHSNavTree;
import org.jboss.as.console.client.widgets.LHSNavTreeItem;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class Navigation {

    static LHSNavTreeItem[] links = new LHSNavTreeItem[] {
      new LHSNavTreeItem("Cell Tables", "widgets;show=tables")
    };

    public Widget asWidget() {

        VerticalPanel stack = new VerticalPanel();
        stack.setStyleName("fill-layout");

        DisclosurePanel panel = new DisclosureStackHeader("Widgets").asWidget();

        LHSNavTree tree = new LHSNavTree("widgets");
        for(LHSNavTreeItem link : links)
            tree.addItem(link);

        panel.setContent(tree);

        stack.add(panel);
        return stack;
    }
}
