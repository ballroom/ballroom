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

package org.jboss.ballroom.client.widgets.tables;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.Collections;
import java.util.List;

/**
 * Default cell table (styles).
 *
 * @author Heiko Braun
 * @date 2/22/11
 */
public class DefaultCellTable<T> extends CellTable {

    public static final DefaultCellTableResources DEFAULT_CELL_TABLE_RESOURCES =
            new DefaultCellTableResources();
    private static final String CELLTABLE_EMPTY_DIV = "celltable-empty-div";

    private boolean isEmpty = false;
    private boolean isEnabled = false;

    private T lastSelection = null;

    public interface RowOverHandler {
        void onRowOver(int rowNum);

        void onRowOut(int rowNum);
    }

    private RowOverHandler rowOverHandler = null;

    public DefaultCellTable(int pageSize) {
        super(pageSize, DEFAULT_CELL_TABLE_RESOURCES);
        initDefaults();
    }

    public DefaultCellTable(int pageSize, ProvidesKey<T> keyProvider) {

        super(pageSize, DEFAULT_CELL_TABLE_RESOURCES, keyProvider);
        initDefaults();
    }

    private void initDefaults() {
        setStyleName("default-cell-table");
        setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        // default empty
        setRowCount(0);
        setRowData(0, Collections.EMPTY_LIST);

        getElement().setAttribute("role", "grid");
    }

    @Override
    public void setRowCount(int size, boolean isExact) {
        super.setRowCount(size, isExact);
    }

    private void setEmpty(boolean isEmpty)
    {
        if(this.isEmpty == isEmpty) return;

        if(isEmpty)
        {
            Element div = DOM.createDiv();
            div.setId(CELLTABLE_EMPTY_DIV);
            div.setInnerHTML("No items!");
            div.setAttribute("style", "text-align:center;width:100%; height:50px; color:#cccccc; padding-top:22px;");
            getElement().appendChild(div);
        }
        else
        {
            Node lastChild = getElement().getLastChild();

            if(lastChild.getNodeType() == Node.ELEMENT_NODE)
            {
                com.google.gwt.dom.client.Element el = (com.google.gwt.dom.client.Element)lastChild;
                if(el.getId().equals(CELLTABLE_EMPTY_DIV))
                   DOM.removeChild(getElement(), (Element)el);
            }

        }

        this.isEmpty = isEmpty;
    }

    @Override
    @Deprecated
    public void setRowData(int start, List values) {
        setEmpty(values.isEmpty());
        super.setRowData(start, values);
    }

    public void setEnabled(boolean b)
    {
        this.isEnabled = b;

        for(int i=0; i<getColumnCount(); i++)
        {
            Cell cell = getColumn(i).getCell();
            if(cell instanceof DefaultEditTextCell)
            {
                DefaultEditTextCell defaultCell = (DefaultEditTextCell)cell;
                defaultCell.setEnabled(b);
            }
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    int hoveredRow = -1;

    @Override
    protected void onBrowserEvent2(Event event) {
        super.onBrowserEvent2(event);

        if(rowOverHandler!=null)
        {
            EventTarget eventTarget = event.getEventTarget();
            String eventType = event.getType();

            if("mouseover".equals(eventType))
            {
                if (!com.google.gwt.dom.client.Element.is(eventTarget)) {
                    return;
                }
                final com.google.gwt.dom.client.Element target = event.getEventTarget().cast();

                hoveredRow = TableUtils.identifyRow(target);
                rowOverHandler.onRowOver(hoveredRow);
            }
            else if ("mouseout".equals(eventType) )
            {
                if(hoveredRow>=0)
                {
                    rowOverHandler.onRowOut(hoveredRow);
                }
            }
        }
    }

    @Override
    public void setSelectionModel(SelectionModel selectionModel) {
        super.setSelectionModel(selectionModel);
        if(selectionModel instanceof SingleSelectionModel)
        {
            final SingleSelectionModel<T> model = ((SingleSelectionModel<T>)selectionModel);
            model.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {

                    lastSelection = model.getSelectedObject();
                }
            });
        }
    }

    public T getPreviousSelectedEntity() {
        return lastSelection;
    }

    public void setRowOverHandler(RowOverHandler rowOverHandler) {
        this.rowOverHandler = rowOverHandler;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * In order for the default selection to work, you need to implement
     * {@link com.google.gwt.view.client.ProvidesKey} on the cell table.
     */
    public void selectDefaultEntity() {

        if(null == getSelectionModel()) return;

        ProvidesKey keyProvider = getKeyProvider();
        boolean didMatch = false;

        if(keyProvider!=null && getPreviousSelectedEntity()!=null)
        {
            Object prevKey = keyProvider.getKey(getPreviousSelectedEntity());
            for(Object entity : getVisibleItems())
            {
                Object currKey = keyProvider.getKey(entity);
                if(prevKey.equals(currKey))
                {
                    getSelectionModel().setSelected(entity, true);
                    didMatch=true;
                    break;
                }

            }
        }


        if(!didMatch && getVisibleItemCount()>0)
        {
            lastSelection=null;
            getSelectionModel().setSelected(getVisibleItem(0), true);
        }
    }
}


