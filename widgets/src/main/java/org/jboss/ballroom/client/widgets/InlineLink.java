package org.jboss.ballroom.client.widgets;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author Heiko Braun
 * @date 12/15/11
 */
public class InlineLink extends HTML {

    public InlineLink(String title) {
        super("<a href='javascript:void(0)' style='vertical-align:bottom;padding-left:5px;'>"+title+"</a>");
        addStyleName("inline-link");
    }
}

