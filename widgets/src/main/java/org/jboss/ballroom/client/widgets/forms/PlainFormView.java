package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heiko Braun
 * @date 11/9/11
 */
public class PlainFormView {

    private static final FormItemTableResources DEFAULT_CELL_TABLE_RESOURCES =
            new FormItemTableResources();

    private CellTable<Row> table;
    private List<FormItem> items;
    private List<Row> rows;
    private int numColumns = 2;
    private boolean hasEntity = false;
    private final static String EMPTY_STRING = "";

    public PlainFormView(List<FormItem> items) {
        this.items = items;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public Widget asWidget(RenderMetaData metaData) {

        table = new CellTable<Row>(20, DEFAULT_CELL_TABLE_RESOURCES);
        table.setStyleName("form-item-table");

        for(int col = 0; col<numColumns; col++)
        {
            final int currentCol = col;

            Column titleCol = new Column<Row, String>(new TitleCell()) {
                @Override
                public String getValue(Row row) {
                    FormItem item = row.get(currentCol);
                    return item!=null ? item.getTitle() : "";
                }
            };


            Column valueCol = new TextColumn<Row>() {
                @Override
                public String getValue(Row row) {
                    FormItem item = row.get(currentCol);
                    return item!=null ? valueRepresentation(item) : "";
                }
            };

            if(numColumns==1)
            {
                table.setColumnWidth(titleCol, 20, Style.Unit.PCT);
                table.setColumnWidth(valueCol, 80, Style.Unit.PCT);
            }
            else
            {
                // default columns layout
                int colWidth = 100/(numColumns*2);

                table.setColumnWidth(titleCol, colWidth-10, Style.Unit.PCT);
                table.setColumnWidth(valueCol, colWidth + 10, Style.Unit.PCT);
            }

            table.addColumn(titleCol);
            table.addColumn(valueCol);

        }

        //table.setTableLayoutFixed(true);

        table.setEmptyTableWidget(new HTML());
        table.setLoadingIndicator(new HTML());

        rows = groupItems();

        return table;
    }

    private String valueRepresentation(FormItem item) {

        String represenation = null;
        Object value = item.getValue();

        if(item.isUndefined())
        {
            represenation = EMPTY_STRING;
        }
        else if(value instanceof Boolean)
        {
            String booleanFallback = hasEntity ? "false" : EMPTY_STRING;
            represenation = (Boolean)value ? "true" : booleanFallback;
        }
        else
        {
            represenation = hasEntity ? String.valueOf(value) : EMPTY_STRING;
        }

        return represenation;
    }

    public void refresh(boolean hasEntity) {

        this.hasEntity = hasEntity; // changes the display style (no entity at all != empty entity)

        table.setRowCount(rows.size(), true);
        table.setRowData(rows);
    }

    private List<Row> groupItems() {
        List<Row> rows = new ArrayList<Row>();

        int i=0;
        while(i<items.size())
        {
            FormItem[] itemsPerRow = new FormItem[numColumns];

            for(int col=0; col<numColumns;col++)
            {
                if(i+col>=items.size())
                    itemsPerRow[col] = null;
                else
                    itemsPerRow[col] = items.get(i+col);
            }

            rows.add(new Row(itemsPerRow));

            i+=numColumns;
        }

        return rows;
    }

    private final class Row {

        FormItem[] items;

        Row(FormItem... items) {
            this.items = items;
        }

        public FormItem get(int i) {
            return items[i];
        }
    }


    interface Template extends SafeHtmlTemplates {
        @Template("<div class='form-item-title' style='outline:none;'>{0}: </div>")
        SafeHtml render(String title);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

    private class TitleCell extends AbstractCell<String> {

        @Override
        public void render(
                Cell.Context context,
                String title,
                SafeHtmlBuilder safeHtmlBuilder)
        {

            boolean hasTitle = title!=null && !title.equals("");
            SafeHtml render = hasTitle ? TEMPLATE.render(title) : new SafeHtmlBuilder().toSafeHtml();
            safeHtmlBuilder.append(render);

        }

    }

}
