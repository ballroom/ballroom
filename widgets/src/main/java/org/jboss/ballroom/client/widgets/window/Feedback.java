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

package org.jboss.ballroom.client.widgets.window;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.ballroom.client.I18n;

/**
 * Collection of feedback windows.
 * Info, Confirmation, Alert, etc.
 * @author Heiko Braun
 * @date 3/2/11
 */
public class Feedback {

    public static void confirm(String title, String message, final ConfirmationHandler handler)
    {
        final DefaultWindow window = new DefaultWindow(title);

        int width = 320;
        int height = 240;

        window.setWidth(width);
        window.setHeight(height);

        window.setGlassEnabled(true);

        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("default-window-content");

        HTML text = new HTML(message);
        panel.add(text);

        ClickHandler confirmHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                handler.onConfirmation(true);
                window.hide();
            }
        };

        ClickHandler cancelHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                handler.onConfirmation(false);
                window.hide();
            }
        };

        DialogueOptions options = new DialogueOptions(I18n.CONSTANTS.common_label_confirm(), confirmHandler, I18n.CONSTANTS.common_label_cancel(), cancelHandler);

        Widget content = new WindowContentBuilder(panel, options).build();

        window.setWidget(content);

        window.center();
    }

    @Deprecated
    public static void alert(String title, String message)
    {
        alert(title, new SafeHtmlBuilder().appendEscaped(message).toSafeHtml());
    }

    public static void alert(String title, SafeHtml message)
    {
        final DefaultWindow window = new DefaultWindow(title);

        int width = 320;
        int height = 240;

        window.setWidth(width);
        window.setHeight(height);

        window.setGlassEnabled(true);

        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("default-window-content");

        HTML text = new HTML(message);
        panel.add(text);

        ClickHandler confirmHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                window.hide();
            }
        };

        DialogueOptions options = new DialogueOptions(I18n.CONSTANTS.common_label_confirm(), confirmHandler, I18n.CONSTANTS.common_label_cancel(), confirmHandler);

        Widget content = new WindowContentBuilder(panel, options.showCancel(false)).build();

        window.setWidget(content);

        window.center();
    }

    public static PopupPanel loading(String title, String message, final LoadingCallback callback) {

        final DefaultWindow window = new DefaultWindow(title);

        int width = 320;
        int height = 240;

        window.setWidth(width);
        window.setHeight(height);

        window.setGlassEnabled(true);

        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("default-window-content");

        HTML text = new HTML(message);
        panel.add(text);

        ClickHandler confirmHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                window.hide();
            }
        };

        ClickHandler cancelHandler = new ClickHandler() {

            public void onClick(ClickEvent event) {
                callback.onCancel();
            }
        };

        //DialogueOptions options = new DialogueOptions("OK", confirmHandler, "Cancel", cancelHandler);

        Widget content = new WindowContentBuilder(panel, new HTML()).build();

        window.setWidget(content);

        window.center();

        return window;
    }

    public interface LoadingCallback {
        void onCancel();
    }

    public interface ConfirmationHandler
    {
        void onConfirmation(boolean isConfirmed);
    }
}
