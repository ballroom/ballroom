package org.jboss.ballroom.client.widgets.tables.pager;

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
    @Source("caret-right.png")
    ImageResource simplePagerFastForward();

    /**
     * The disabled "fast forward" image.
     */
    @Source("caret-right_disabled.png")
    ImageResource simplePagerFastForwardDisabled();

    /**
     * The image used to go to the first page.
     */
    @Source("backward.png")
    ImageResource simplePagerFirstPage();

    /**
     * The disabled first page image.
     */
    @Source("backward_disabled.png")
    ImageResource simplePagerFirstPageDisabled();

    /**
     * The image used to go to the last page.
     */
    @Source("forward.png")
    ImageResource simplePagerLastPage();

    /**
     * The disabled last page image.
     */
    @Source("forward_disabled.png")
    ImageResource simplePagerLastPageDisabled();

    /**
     * The image used to go to the next page.
     */
    @Source("caret-right.png")
    ImageResource simplePagerNextPage();

    /**
     * The disabled next page image.
     */
    @Source("caret-right_disabled.png")
    ImageResource simplePagerNextPageDisabled();

    /**
     * The image used to go to the previous page.
     */
    @Source("caret-left.png")
    ImageResource simplePagerPreviousPage();

    /**
     * The disabled previous page image.
     */
     @Source("caret-left_disabled.png")
    ImageResource simplePagerPreviousPageDisabled();

    /**
     * The styles used in this widget.
     */
    @Source("DefaultPager.css")
    SimplePager.Style simplePagerStyle();
}