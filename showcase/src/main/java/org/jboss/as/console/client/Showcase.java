package org.jboss.as.console.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import org.jboss.as.console.client.gin.ShowcaseUI;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public class Showcase implements EntryPoint {

    public final static ShowcaseUI MODULES = GWT.create(ShowcaseUI.class);

    public void onModuleLoad() {
        // Defer all application initialisation code to onModuleLoad2() so that the
        // UncaughtExceptionHandler can catch any unexpected exceptions.
        Log.setUncaughtExceptionHandler();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                onModuleLoad2();
            }
        });
    }

    public void onModuleLoad2() {

        final Image loadingImage = new Image("images/loading_lite.gif");
        loadingImage.getElement().setAttribute("style", "margin-top:200px;margin-left:auto;margin-right:auto;");

        RootLayoutPanel.get().add(loadingImage);


        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                Window.alert("Code download failed");
            }

            public void onSuccess() {
                DelayedBindRegistry.bind(MODULES);

                RootLayoutPanel.get().remove(loadingImage);

                MODULES.getPlaceManager().revealDefaultPlace();
            }
        });
    }

}
