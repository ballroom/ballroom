package org.jboss.ballroom.client.widgets.tables;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.SimplePager;

/**
 * @author Heiko Braun
 * @date 1/30/12
 */
public interface PagerResources extends SimplePager.Resources {

    /**
     * The image used to skip ahead multiple pages.
     */
    @Source("next.png")
    ImageResource simplePagerFastForward();

    /**
     * The disabled "fast forward" image.
     */
    @Source("next_dis.png")
    ImageResource simplePagerFastForwardDisabled();

    /**
     * The image used to go to the first page.
     */
    @Source("first.png")
    ImageResource simplePagerFirstPage();

    /**
     * The disabled first page image.
     */
    @Source("first_dis.png")
    ImageResource simplePagerFirstPageDisabled();

    /**
     * The image used to go to the last page.
     */
    @Source("last.png")
    ImageResource simplePagerLastPage();

    /**
     * The disabled last page image.
     */
    @Source("last_dis.png")
    ImageResource simplePagerLastPageDisabled();

    /**
     * The image used to go to the next page.
     */
    @Source("next.png")
    ImageResource simplePagerNextPage();

    /**
     * The disabled next page image.
     */
    @Source("next_dis.png")
    ImageResource simplePagerNextPageDisabled();

    /**
     * The image used to go to the previous page.
     */
    @Source("previous.png")
    ImageResource simplePagerPreviousPage();

    /**
     * The disabled previous page image.
     */
     @Source("previous_dis.png")
    ImageResource simplePagerPreviousPageDisabled();

    /**
     * The styles used in this widget.
     */
    @Source("DefaultPager.css")
    SimplePager.Style simplePagerStyle();
}