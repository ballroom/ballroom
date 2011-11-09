package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTableStyle;

/**
 * @author Heiko Braun
 * @date 11/9/11
 */
public class FormItemTableResources implements CellTable.Resources
{
    CellTable.Resources real = GWT.create(CellTable.Resources.class);

    public ImageResource cellTableFooterBackground() {
        return real.cellTableFooterBackground();
    }

    @Override
    public ImageResource cellTableHeaderBackground() {
        return real.cellTableHeaderBackground();
    }

    @Override
    public ImageResource cellTableLoading() {
        return real.cellTableLoading();
    }

    @Override
    public ImageResource cellTableSelectedBackground() {
        return real.cellTableSelectedBackground();
    }

    @Override
    public ImageResource cellTableSortAscending() {
        return real.cellTableSortAscending();
    }

    @Override
    public ImageResource cellTableSortDescending() {
        return real.cellTableSortDescending();
    }

    public com.google.gwt.user.cellview.client.CellTable.Style cellTableStyle() {
        return new FormItemTableStyle();
    }
}
