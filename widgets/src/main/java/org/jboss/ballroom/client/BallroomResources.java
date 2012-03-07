package org.jboss.ballroom.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * @author Heiko Braun
 * @date 3/7/12
 */
public interface BallroomResources extends ClientBundle {

    public static final BallroomResources INSTANCE =  GWT.create(BallroomResources.class);

    @CssResource.NotStrict
    @Source("org/jboss/ballroom/public/plain.css")
    public CssResource plainCss();

    @CssResource.NotStrict
    @Source("org/jboss/ballroom/public/redhat.css")
    public CssResource redhatCss();
}
