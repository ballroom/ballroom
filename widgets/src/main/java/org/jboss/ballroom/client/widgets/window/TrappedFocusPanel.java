package org.jboss.ballroom.client.widgets.window;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that trap focus on it's child contents.
 *
 * @see Focus
 *
 * @author Heiko Braun
 * @date 3/12/12
 */
public class TrappedFocusPanel extends LayoutPanel {

    protected Focus focus;

    public TrappedFocusPanel() {
        super();
        setStyleName("fill-layout");
        this.sinkEvents(Event.ONKEYDOWN);

        focus = new Focus(getElement());

    }

    public TrappedFocusPanel(Widget child) {
        super();
        setStyleName("fill-layout");
        this.sinkEvents(Event.ONKEYDOWN);
        this.sinkEvents(Event.ONMOUSEDOWN);
        add(child);

        focus = new Focus(getElement());

    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Scheduler.get().scheduleDeferred(
                new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        //focus = new Focus(getElement());
                        focus.onFirstInput();
                    }
                }
        );

    }

    public Focus getFocus() {
        return focus;
    }

    @Override
    public void onBrowserEvent(Event event) {

        int type = DOM.eventGetType(event);
        switch (type) {
            case Event.ONKEYDOWN:
                if(event.getKeyCode()== KeyCodes.KEY_TAB)
                {
                    event.preventDefault();
                    event.stopPropagation();

                    if(event.getShiftKey())
                        focus.prev();
                    else
                        focus.next();

                }
                break;
            case Event.ONMOUSEDOWN:
                Element element = event.getEventTarget().cast();
                focus.onElement(element);
                break;
            default:
                return;

        }

    }
}

