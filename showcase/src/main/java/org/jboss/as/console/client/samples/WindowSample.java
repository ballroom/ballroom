package org.jboss.as.console.client.samples;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.widgets.ContentHeaderLabel;
import org.jboss.as.console.client.widgets.DefaultButton;
import org.jboss.as.console.client.widgets.DefaultWindow;
import org.jboss.as.console.client.widgets.DialogueOptions;
import org.jboss.as.console.client.widgets.WindowContentBuilder;
import org.jboss.as.console.client.widgets.forms.FormValidation;

/**
 * @author Heiko Braun
 * @date 7/13/11
 */
public class WindowSample implements Sample {

    private DefaultWindow window;

    @Override
    public String getName() {
        return "Windows";
    }

    @Override
    public String getId() {
        return "windows";
    }

    @Override
    public Widget asWidget() {

        VerticalPanel layout = new VerticalPanel();
        layout.addStyleName("rhs-content-panel");

        // desc
        HTML desc = new HTML();
        desc.setHTML("<p>Windows that can be resized and dragged. Click the button to open a window");
        layout.add(desc);

        DefaultButton button = new DefaultButton("Open Window", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                openWindow();
            }
        });
        layout.add(button);

        return layout;
    }

    private void openWindow() {
        window = new DefaultWindow("Example Window");
        window.setWidth(480);
        window.setHeight(360);

        window.setWidget( new WindowContent().asWidget());

        window.setGlassEnabled(true);
        window.center();
    }

    class WindowContent {
        Widget asWidget() {

            VerticalPanel layout = new VerticalPanel();
            layout.add(new HTML("Try dragging and resizing me. ESC closes me."));

            DialogueOptions options = new DialogueOptions(

                    // save
                    new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            // save callback
                        }
                    },

                    // cancel
                    new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            // cancel callback
                            window.hide();
                        }
                    }

            );

            return new WindowContentBuilder(layout, options).build();

        }
    }
}
