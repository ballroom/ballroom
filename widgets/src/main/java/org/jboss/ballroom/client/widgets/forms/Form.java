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

package org.jboss.ballroom.client.widgets.forms;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanCodex;
import com.google.gwt.autobean.shared.AutoBeanFactory;
import com.google.gwt.autobean.shared.AutoBeanUtils;
import com.google.gwt.autobean.shared.AutoBeanVisitor;
import com.google.gwt.autobean.shared.Splittable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.jboss.ballroom.client.spi.Framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Form data binding that works on {@link AutoBean} entities.
 *
 * @author Heiko Braun
 * @date 2/21/11
 */
public class Form<T> implements FormAdapter<T> {

    private final static Framework framework = GWT.create(Framework.class);
    private static final String EXPR_TAG = "EXPRESSIONS";

    private AutoBeanFactory factory;
    private final static String DEFAULT_GROUP = "default";

    private final Map<String, Map<String, FormItem>> formItems = new LinkedHashMap<String, Map<String, FormItem>>();
    private final Map<String,GroupRenderer> renderer = new HashMap<String, GroupRenderer>();

    private int numColumns = 1;
    private int nextId = 1;
    private T editedEntity = null;
    private final Class<?> conversionType;

    private List<EditListener> listeners = new ArrayList<EditListener>();

    public Form(Class<?> conversionType) {
        this.conversionType = conversionType;
        this.factory = framework.getBeanFactory();
    }

    @Override
    public Class<?> getConversionType() {
        return conversionType;
    }

    /**
     * Number of layout columns.<br>
     * Form fields will fill columns in the order they have been specified
     * in {@link #setFields(FormItem[])}.
     *
     * @param columns
     */
    public void setNumColumns(int columns)
    {
        this.numColumns = columns;
    }

    /**
     * Specify the form fields.
     * Needs to be called before {@link #asWidget()}.
     *
     * @param items
     */
    public void setFields(FormItem... items) {
        setFieldsInGroup(DEFAULT_GROUP, items);
    }

    public void setFields(FormItem[]... items) {
        setFieldsInGroup(DEFAULT_GROUP, flatten(items));
    }

    int maxTitleLength = 0; // used for auto layout
    public void setFieldsInGroup(String group, FormItem... items) {

        // create new group
        LinkedHashMap<String, FormItem> groupItems = new LinkedHashMap<String, FormItem>();
        formItems.put(group, groupItems);

        for(FormItem item : items)
        {
            String title = item.getTitle();
            if(title.length()>maxTitleLength)
            {
                maxTitleLength = title.length();
            }

            // key maybe be used multiple times
            String itemKey = item.getName();

            if(groupItems.containsKey(itemKey)) {
                groupItems.put(itemKey+"#"+nextId, item);
                nextId++;
            }
            else
            {
                groupItems.put(itemKey, item);
            }
        }
    }

    public void setFieldsInGroup(String group, FormItem[]... items) {
        setFieldsInGroup(group, flatten(items));
    }

    public void setFieldsInGroup(String group, GroupRenderer renderer, FormItem... items) {

        this.renderer.put(group, renderer);

        setFieldsInGroup(group, items);
    }

    private FormItem<?>[] flatten(FormItem<?>[]... items) {
        List<FormItem<?>> l = new ArrayList<FormItem<?>>();
        for (FormItem<?> [] fiArray : items) {
            for (FormItem<?> fi : fiArray) {
                l.add(fi);
            }
        }
        FormItem<?>[] array = l.toArray(new FormItem<?>[l.size()]);
        return array;
    }

    /**
     * This method passes the original entity back into the form, removing all changes.
     */
    @Override
    public void cancel() {
        edit(editedEntity);
    }

    @Override
    public void edit(T bean) {

        // Needs to be declared (i.e. when creating new instances)
        if(null==bean)
            throw new IllegalArgumentException("Invalid entity: null");

        // Has to be an AutoBean
        final AutoBean<T> autoBean = asAutoBean(bean);

        this.editedEntity = bean;


        final Map<String, String> exprMap = getExpressions(editedEntity);

        autoBean.accept(new AutoBeanVisitor() {

            private boolean isComplex = false;

            @Override
            public boolean visitValueProperty(final String propertyName, final Object value, PropertyContext ctx) {

                if(isComplex ) return true; // skip complex types

                visitItem(propertyName, new FormItemVisitor() {
                    @Override
                    public void visit(FormItem item) {

                        item.resetMetaData();

                        // expressions
                       // if(item.doesSupportExpressions())
                        //{
                            String exprValue = exprMap.get(propertyName);
                            if(exprValue!=null)
                            {
                                item.setUndefined(false);
                                item.setExpressionValue(exprValue);
                            }
                        //}

                        // values
                        if(value!=null)
                        {
                            item.setUndefined(false);
                            item.setValue(value);
                        }
                        else
                        {
                            item.setUndefined(true);
                            item.setModified(true); // don't escape validation
                        }
                    }
                });

                return true;
            }

            @Override
            public void endVisitReferenceProperty(String propertyName, AutoBean<?> value, PropertyContext ctx) {
                //System.out.println("end reference "+propertyName);
                isComplex = false;
            }

            @Override
            public boolean visitReferenceProperty(String propertyName, AutoBean<?> value, PropertyContext ctx) {
                isComplex = true;
                //System.out.println("begin reference "+propertyName+ ": "+ctx.getType());
                return true;
            }

            @Override
            public boolean visitCollectionProperty(String propertyName, final AutoBean<Collection<?>> value, CollectionPropertyContext ctx) {

                visitItem(propertyName, new FormItemVisitor() {
                    @Override
                    public void visit(FormItem item) {

                        item.resetMetaData();

                        if(value!=null)
                        {
                            item.setUndefined(false);
                            item.setValue(value.as());
                        }
                        else
                        {
                            item.setUndefined(true);
                            item.setModified(true); // don't escape validation
                        }
                    }
                });

                return true;
            }

            @Override
            public void endVisitCollectionProperty(String propertyName, AutoBean<Collection<?>> value, CollectionPropertyContext ctx) {
                super.endVisitCollectionProperty(propertyName, value, ctx);
            }
        });

        notifyListeners(bean);
    }

    private void notifyListeners(T bean) {
        for (EditListener listener : listeners) {
            listener.editingBean(bean);
        }
    }

    @Override
    public void addEditListener(EditListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeEditListener(EditListener listener) {
        this.listeners.remove(listener);
    }

    void visitItem(final String name, FormItemVisitor visitor) {
        String namePrefix = name + "_";
        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(String key : groupItems.keySet())
            {
                if(key.equals(name) || key.startsWith(namePrefix))
                {
                    visitor.visit(groupItems.get(key));
                }
            }
        }
    }

    /**
     * Get changed values since last {@link #edit(Object)} ()}
     * @return
     */
    @Override
    public Map<String, Object> getChangedValues() {

        final T updatedEntity = getUpdatedEntity();
        final T editedEntity = getEditedEntity();

        Map<String, Object> diff = AutoBeanUtils.diff(
                AutoBeanUtils.getAutoBean(editedEntity),
                AutoBeanUtils.getAutoBean(updatedEntity)
        );

        Map<String, Object> finalDiff = new HashMap<String,Object>();

        // map changes, but skip unmodified fields
        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                Object val = diff.get(item.getName());

                // expression have precedence over real values
                if(item.isExpressionValue())
                {
                    finalDiff.put(item.getName(), item.asExpressionValue());
                }

                // regular values
                else if(val!=null && item.isModified())
                {
                    if(item.isUndefined())
                        finalDiff.put(item.getName(), FormItem.UNDEFINED.Value);
                    else
                        finalDiff.put(item.getName(), val);
                }
            }
        }

        return finalDiff;
    }

    @Override
    public FormValidation validate()
    {

        FormValidation outcome = new FormValidation();

        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                boolean validValue = item.validate(item.getValue());
                if(validValue)
                {
                    item.setErroneous(false);
                }
                else
                {
                    outcome.addError(item.getName());
                    item.setErroneous(true);
                }
            }
        }

        return outcome;
    }

    /**
     * This is what the entity looks like with the user's changes on the form.
     */
    @Override
    public T getUpdatedEntity() {

        Map<String,String> exprMap = new HashMap<String,String>();

        StringBuilder builder = new StringBuilder("{");
        int g=0;
        for(Map<String, FormItem> groupItems : formItems.values())
        {
            int i=0;
            for(FormItem item : groupItems.values())
            {
                builder.append("\"");
                builder.append(item.getName());
                builder.append("\"");

                builder.append(":");

                builder.append(encodeValue(item.getValue()));

                if(i<groupItems.size()-1)
                    builder.append(", ");

                i++;


                // Expressions
                if(item.isExpressionValue())
                    exprMap.put(item.getName(), item.asExpressionValue());
            }

            if(g<formItems.size()-1)
                builder.append(", ");

            g++;
        }
        builder.append("}");

        AutoBean<?> decoded = AutoBeanCodex.decode(
                factory,
                conversionType,
                builder.toString()
        );

        decoded.setTag(EXPR_TAG, exprMap);

        return (T) decoded.as();

    }

    private String encodeValue(Object object) {
        StringBuilder sb = new StringBuilder();

        if(object instanceof List)     // list objects
        {
            List listObject = (List)object;
            sb.append("[");
            int c = 0;
            for(Object item : listObject)
            {
                sb.append(encodeValue(item));

                if(c<listObject.size()-1)
                    sb.append(", ");

                c++;
            }
            sb.append("]");
        } else if (AutoBeanUtils.getAutoBean(object) != null) {
            Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
            sb.append("{ ");
            sb.append(encodeValue(split));
            sb.append(" }");
        } else if (object instanceof Splittable) {
            Splittable split = (Splittable)object;
            if (split.isString()) return encodeValue(split.asString());

            int c = 0;
            List<String> keys = split.getPropertyKeys();
            for (String key : keys) {
                sb.append(encodeValue(key));
                sb.append(" : ");
                sb.append(encodeValue(split.get(key)));
                if(c<keys.size()-1)
                    sb.append(", ");
                c++;
            }
        } else {
            sb.append("\"");
            sb.append(object.toString());
            sb.append("\"");
        }

        return sb.toString();
    }

    @Override
    public Widget asWidget() {
        return build();
    }

    private Widget build() {
        VerticalPanel parentPanel = new VerticalPanel();
        parentPanel.setStyleName("fill-layout-width");

        RenderMetaData metaData = new RenderMetaData();
        metaData.setNumColumns(numColumns);
        metaData.setTitleWidth(maxTitleLength);

        for(String group : formItems.keySet())
        {
            Map<String, FormItem> groupItems = formItems.get(group);
            if(DEFAULT_GROUP.equals(group))
            {
                DefaultGroupRenderer defaultGroupRenderer = new DefaultGroupRenderer();

                Widget defaultGroupWidget = defaultGroupRenderer.render(metaData,DEFAULT_GROUP, groupItems);
                parentPanel.add(defaultGroupWidget);
            }
            else
            {
                GroupRenderer groupRenderer = renderer.get(group)!=null ?
                        renderer.get(group) : new FieldsetRenderer();

                Widget widget = groupRenderer.render(metaData, group, groupItems);
                parentPanel.add(widget);
            }
        }

        return parentPanel;
    }

    /**
     * Enable/disable this form.
     *
     * @param b
     */
    @Override
    public void setEnabled(boolean b) {
        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                item.setEnabled(b);
            }
        }
    }

    /**
     * Binds a default single selection model to the table
     * that displays selected rows in a form.
     *
     * @param table
     */
    @Override
    public void bind(CellTable<T> table) {
        SingleSelectionModel<T> selectionModel = (SingleSelectionModel<T>)table.getSelectionModel();
        if (selectionModel == null) {
            selectionModel = new SingleSelectionModel<T>();
        }

        final SingleSelectionModel<T> finalSelectionModel = selectionModel;

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            public void onSelectionChange(SelectionChangeEvent event) {
                edit(finalSelectionModel.getSelectedObject());
            }
        });

        table.setSelectionModel(finalSelectionModel);

        table.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {

            public void onRowCountChange(RowCountChangeEvent event) {
                if(event.getNewRowCount()==0 && event.isNewRowCountExact())
                    clearValues();

            }
        });

    }

    public void clearValues() {
        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                item.clearValue();
            }
        }

    }

    /**
     * This is the entity that was originally passed in for editing.  It does not include
     * changes made by the user.
     *
     * @return The original entity used for editing.
     */
    public T getEditedEntity() {
        return editedEntity;
    }

    interface FormItemVisitor {
        void visit(FormItem item);
    }


    public List<String> getFormItemNames() {
        List<String> result = new ArrayList<String>();

        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                result.add(item.getName());
            }
        }

        return result;
    }


    public static Map<String,String> getExpressions(Object bean)
    {
        final AutoBean autoBean = asAutoBean(bean);

        Map<String, String> exprMap = (Map<String,String>)autoBean.getTag(EXPR_TAG);
        if(null==exprMap)
        {
            exprMap = new HashMap<String,String>();
            autoBean.setTag(EXPR_TAG, exprMap);
        }

        return exprMap;
    }

    private static AutoBean asAutoBean(Object bean) {
        final AutoBean autoBean = AutoBeanUtils.getAutoBean(bean);
        if(null==autoBean)
            throw new IllegalArgumentException("Not an auto bean: " + bean.getClass());
        return autoBean;
    }

}
