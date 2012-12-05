package org.jboss.ballroom.client.layout;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * A LHS navigation tree section (bold header look&feel).
 */
public class LHSTreeSection extends TreeItem {

    public LHSTreeSection(String title) {
        this(title, false);
    }

    public LHSTreeSection(String title, boolean first) {

        addStyleName("tree-section");

        setText(title);

        if(first)
        {
            addStyleName("tree-section-first");
            getElement().setAttribute("tabindex", "-1");
        }
    }



    @Override
    public void setSelected(boolean selected) {

        super.setSelected(selected);
        if(selected)
            addStyleName("tree-section-selected");
        else
            removeStyleName("tree-section-selected");
    }


}

