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
import com.google.gwt.user.client.DOM;
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

    private final String id = "formview-"+ DOM.createUniqueId()+"_";

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

        // see http://www.w3.org/TR/wai-aria/roles#group
        // ... when a group is used in the context of list, authors MUST limit its children to listitem elements

       // table.getElement().setAttribute("role", "group");


        for(int col = 0; col<numColumns; col++)
        {
            final int currentCol = col;

            Column titleCol = new Column<Row, String>(new TitleCell(currentCol)) {
                @Override
                public String getValue(Row row) {
                    FormItem item = row.get(currentCol);
                    return item!=null ? item.getTitle() : "";
                }
            };


            Column valueCol = new Column<Row, String>(new ValueCell(currentCol)) {
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
        else if(hasEntity && item.isExpressionValue())
        {
            represenation = String.valueOf(item.asExpressionValue());
        }
        else if(hasEntity && (item instanceof PasswordBoxItem))
        {
            // hide passwords
            StringBuffer sb = new StringBuffer();
            String plainText = String.valueOf(value);
            for(int i=0; i<plainText.length(); i++)
                sb.append("*");
            represenation = sb.toString();
        }
        else if(hasEntity && (value instanceof Boolean))
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

            rows.add(new Row(i, itemsPerRow));

            i+=numColumns;
        }

        return rows;
    }

    private boolean isHyperlink(final String value)
    {
        return value != null &&
                (value.toLowerCase().startsWith("http://") || value.toLowerCase().startsWith("https://"));
    }

    private final class Row {

        FormItem[] items;
        private int rowNum;

        Row(int rowNum, FormItem... items) {
            this.rowNum = rowNum;
            this.items = items;
        }

        public FormItem get(int i) {
            return items[i];
        }

        public int getRowNum() {
            return rowNum;
        }
    }


    interface Template extends SafeHtmlTemplates {
        @Template("<div class='form-item-title' style='outline:none;' id='{0}'>{1}: </div>")
        SafeHtml render(String id, String title);
    }

    interface ValueTemplate extends SafeHtmlTemplates {
        @Template("<span aria-labelledBy='{0}'>{1}</span>")
        SafeHtml render(String id, String title);
    }

    interface HyperlinkTemplate extends SafeHtmlTemplates {
        @Template("<a aria-labelledBy='{0}' tabindex=\"-1\" class='gwt-Hyperlink' href='{1}' target='_blank'>{1}</a>")
        SafeHtml render(String id, String title);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);
    private static final ValueTemplate VALUE_TEMPLATE = GWT.create(ValueTemplate.class);
    private static final HyperlinkTemplate HYPERLINK_TEMPLATE = GWT.create(HyperlinkTemplate.class);

    private class TitleCell extends AbstractCell<String> {

        private int index;

        public TitleCell(int index) {
            super();
            this.index = index;
        }

        @Override
        public void render(Cell.Context context, String title, SafeHtmlBuilder safeHtmlBuilder)
        {
            SafeHtml render;
            Row row = (Row)context.getKey();
            final String labelId = id + row.getRowNum() +"_"+index+"_l";
            boolean hasTitle = title!=null && !title.equals("");
            if (hasTitle)
            {
                render = TEMPLATE.render(labelId, title);
            }
            else
            {
                render = new SafeHtmlBuilder().toSafeHtml();
            }
            safeHtmlBuilder.append(render);
        }
    }

    private class ValueCell extends AbstractCell<String> {

        private int index;

        private ValueCell(int index) {
            super();
            this.index = index;
        }

        @Override
        public void render(Cell.Context context, String value, SafeHtmlBuilder safeHtmlBuilder)
        {
            SafeHtml render;
            Row row = (Row)context.getKey();
            final String labelId = id + row.getRowNum() +"_"+index+"_l";
            boolean hasValue = value!=null && !value.equals("");

            if (hasValue)
            {
                if (isHyperlink(value))
                {
                    render = HYPERLINK_TEMPLATE.render(labelId, value);
                }
                else
                {
                    render = VALUE_TEMPLATE.render(labelId, value);
                }
            }
            else
            {
                render = new SafeHtmlBuilder().toSafeHtml();
            }
            safeHtmlBuilder.append(render);
        }
    }
}
