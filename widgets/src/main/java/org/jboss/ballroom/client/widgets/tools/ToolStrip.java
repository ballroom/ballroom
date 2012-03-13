/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.ballroom.client.widgets.tools;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.ballroom.client.widgets.forms.ComboBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Heiko Braun
 * @date 2/28/11
 */
public class ToolStrip extends FocusPanel {

    private HorizontalPanel left;
    private HorizontalPanel right;

    private HorizontalPanel layout;
    private int currBtnIdx = -1;
    private LinkedList<ToolButton> buttons = new LinkedList<ToolButton>();

    public ToolStrip() {
        super();

        setTabIndex(-1);

        layout = new HorizontalPanel();
        layout.setStyleName("default-toolstrip");

        left = new HorizontalPanel();
        right = new HorizontalPanel();

        layout.add(left);
        layout.add(right);

        left.getElement().getParentElement().setAttribute("width", "50%");
        right.getElement().getParentElement().setAttribute("width", "50%");
        right.getElement().getParentElement().setAttribute("align", "right");

        add(layout);

        addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode()== KeyCodes.KEY_DOWN)
                {
                    nextButton();
                    event.stopPropagation();
                }
                else if(event.getNativeKeyCode()== KeyCodes.KEY_UP)
                {
                    prevButton();
                    event.stopPropagation();
                }
            }
        }) ;
    }

    private void prevButton() {
        if(buttons.isEmpty()) return;

        // remove focus on current selection
        if(currBtnIdx>=0 && currBtnIdx<buttons.size())
            blur(buttons.get(currBtnIdx));

        // focus on next button
        int prev = currBtnIdx-1;

        if(prev<0)
            prev=buttons.size()-1;

        currBtnIdx=prev;

        focus(buttons.get(prev));

    }

    private void nextButton() {

        if(buttons.isEmpty()) return;

        // remove focus on current selection
        if(currBtnIdx>=0 && currBtnIdx<buttons.size())
            blur(buttons.get(currBtnIdx));

        // focus on next button
        int next = currBtnIdx+1;

        if(next >= buttons.size())
            next=0;

        currBtnIdx=next;

        focus(buttons.get(next));

    }

    private void blur(Widget widget) {
        widget.getElement().blur();
        widget.getElement().setAttribute("tabindex", "-1");
    }

    private void focus(Widget widget) {
        widget.getElement().focus();
        widget.getElement().setAttribute("tabindex", "0");
    }

    public void addToolButton(ToolButton button)
    {
        left.add(button);
        buttons.add(button);
    }

    public void addToolButtonRight(ToolButton button)
    {
        button.getElement().setAttribute("style", "margin-right:5px;");
        button.addStyleName("toolstrip-button-secondary");
        right.add(button);
        buttons.add(button);
    }

    public boolean hasButtons() {
        return left.getWidgetCount()>0 || right.getWidgetCount()>0;
    }

    public void addToolWidget(Widget widget) {
        left.add(widget);
    }

    public void addToolWidgetRight(Widget widget) {

        right.add(widget);
    }
}
