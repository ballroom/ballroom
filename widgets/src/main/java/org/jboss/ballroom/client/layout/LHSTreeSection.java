package org.jboss.ballroom.client.layout;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
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

        /*HTMLPanel inner = new HTMLPanel("<span>"+title+"</span>")
        {
            {
                sinkEvents(Event.ONKEYDOWN);
                sinkEvents(Event.ONMOUSEDOWN);

            }
            @Override
            public void onBrowserEvent(Event event) {
                LHSTreeSection.this.setState(!LHSTreeSection.this.getState());
                event.preventDefault();
                event.stopPropagation();
            }
        };

        setWidget(inner);    */

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

