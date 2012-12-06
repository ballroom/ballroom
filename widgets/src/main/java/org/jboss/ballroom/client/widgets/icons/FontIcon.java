package org.jboss.ballroom.client.widgets.icons;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author Heiko Braun
 * @date 12/6/12
 */
public class FontIcon extends HTML {

    public enum Size {
        TINY, SMALL,MEDIUM,LARGE;
    }

    public FontIcon(String name) {
        this(name, Size.SMALL);
    }

    public FontIcon(String name, Size size) {
        super();

        String font = null;

        switch (size)
        {
            case TINY:
                font="font-size:10px!important";
                break;
            case SMALL:
                font="font-size:12px!important";
                break;
            case MEDIUM:
                font="font-size:14px!important";
                break;
            case LARGE:
                font="font-size:18px!important";
                break;

        }
        setHTML("<i class='"+name+"' style='"+font+"'></i>");
    }
}

