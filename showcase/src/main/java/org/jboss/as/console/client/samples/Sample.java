package org.jboss.as.console.client.samples;


import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 7/12/11
 */
public interface Sample {
    String getName();
    Widget asWidget();
}
