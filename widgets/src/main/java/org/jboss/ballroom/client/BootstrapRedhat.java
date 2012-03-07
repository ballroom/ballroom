package org.jboss.ballroom.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * @author Heiko Braun
 * @date 3/7/12
 */
public class BootstrapRedhat implements EntryPoint {
    @Override
    public void onModuleLoad() {
         BallroomResources.INSTANCE.redhatCss().ensureInjected();
    }
}
