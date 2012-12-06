package org.jboss.ballroom.client.widgets.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import org.jboss.ballroom.client.widgets.icons.FontIcon;
import org.jboss.ballroom.client.widgets.icons.Icons;

public class WindowHeader extends Composite implements HasAllMouseHandlers {

    private int origWidth   = -1;
    private int origHeight  = -1;
    private int origTop     = -1;
    private int origLeft    = -1;
    String headerId;

    public WindowHeader(String title, final PopupPanel callback) {

        final HorizontalPanel header = new HorizontalPanel();
        header.setStyleName("default-window-header");

        HTML titleText = new HTML(title);
        headerId = "h_" + DOM.createUniqueId();
        titleText.getElement().setId(headerId);
        titleText.setStyleName("default-window-title");

        FontIcon closeIcon = new FontIcon(" icon-remove", FontIcon.Size.MEDIUM);
        closeIcon.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent clickEvent) {
                callback.hide();
            }
        });

        FontIcon maximizeIcon = new FontIcon("icon-resize-full", FontIcon.Size.MEDIUM);
        maximizeIcon.addClickHandler(new ClickHandler(){
            @Override
            public void onClick(ClickEvent clickEvent) {

                int width = origWidth;
                int height = origHeight;

                int top = origTop;
                int left = origLeft;

                if(origWidth==-1)
                {
                    origWidth = getOffsetWidth();
                    origHeight = (int) ( origWidth / 1.618 ) +50;// TODO: this fails "getOffsetHeight()";
                    origLeft = getAbsoluteLeft();
                    origTop = getAbsoluteTop();

                    width = Window.getClientWidth() - 50;
                    height = Window.getClientHeight() - 50;

                    top = 25;
                    left = 25;
                }
                else
                {
                    origWidth = -1;
                    origHeight = -1;
                    origLeft = -1;
                    origTop = -1;

                }

                callback.hide();

                callback.setPopupPosition(top, left);
                callback.setWidth(width+"px");
                callback.setHeight(height+"px");

                callback.show();

            }
        });

        header.add(titleText);
        header.add(maximizeIcon);
        header.add(closeIcon);

        initWidget(header);

        titleText.getElement().getParentElement().setAttribute("width", "100%");

        maximizeIcon.getElement().getParentElement().setAttribute("width", "16px");
        maximizeIcon.getElement().getParentElement().setAttribute("style", "color:#ffffff;padding-right:10px");

        closeIcon.getElement().getParentElement().setAttribute("width", "16px");
        closeIcon.getElement().getParentElement().setAttribute("style", "color:#ffffff;padding-right:10px");

    }

    public String getHeaderId() {
        return headerId;
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler( handler,
                MouseMoveEvent.getType() );
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler( handler,
                MouseOutEvent.getType() );
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler( handler,
                MouseOverEvent.getType() );
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler( handler,
                MouseUpEvent.getType() );
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addDomHandler( handler,
                MouseWheelEvent.getType() );
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler( handler,
                MouseDownEvent.getType() );
    }

}