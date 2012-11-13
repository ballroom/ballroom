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

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.AutoBeanVisitor;
import com.google.web.bindery.autobean.shared.Splittable;
import org.jboss.ballroom.client.spi.Framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Form data binding that works on {@link AutoBean} entities.
 *
 * @author Heiko Braun
 * @date 2/21/11
 */
public class Form<T> extends AbstractForm<T> {

    private final static Framework framework = GWT.create(Framework.class);
    private static final String EXPR_TAG = "EXPRESSIONS";

    private AutoBeanFactory factory;

    private T editedEntity = null;
    private final Class<?> conversionType;

    private List<EditListener> listeners = new ArrayList<EditListener>();


    public Form(Class<?> conversionType) {
        this.conversionType = conversionType;
        this.factory = framework.getBeanFactory();
    }

    
    public Class<?> getConversionType() {
        return conversionType;
    }

    /**
     * This method passes the original entity back into the form, removing all changes.
     */
    
    public void cancel() {

        if(editedEntity!=null)
            edit(editedEntity);
    }

    
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

            
            public boolean visitValueProperty(final String propertyName, final Object value, PropertyContext ctx) {

                if(isComplex ) return true; // skip complex types

                visitItem(propertyName, new FormItemVisitor() {
                    
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
                        else if(value!=null)
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

            
            public void endVisitReferenceProperty(String propertyName, AutoBean<?> value, PropertyContext ctx) {
                //System.out.println("end reference "+propertyName);
                isComplex = false;
            }

            
            public boolean visitReferenceProperty(String propertyName, AutoBean<?> value, PropertyContext ctx) {
                isComplex = true;
                //System.out.println("begin reference "+propertyName+ ": "+ctx.getType());
                return true;
            }

            
            public boolean visitCollectionProperty(String propertyName, final AutoBean<Collection<?>> value, CollectionPropertyContext ctx) {

                visitItem(propertyName, new FormItemVisitor() {
                    
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

            
            public void endVisitCollectionProperty(String propertyName, AutoBean<Collection<?>> value, CollectionPropertyContext ctx) {
                super.endVisitCollectionProperty(propertyName, value, ctx);
            }
        });

        notifyListeners(bean);


        // plain views
        refreshPlainView();
    }

    private void notifyListeners(T bean) {
        for (EditListener listener : listeners) {
            listener.editingBean(bean);
        }
    }

    
    public void addEditListener(EditListener listener) {
        this.listeners.add(listener);
    }

    
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
    
    public Map<String, Object> getChangedValues() {

        final T editedEntity = getEditedEntity();
        if(null==editedEntity)
            return new HashMap<String, Object>();

        final T updatedEntity = getUpdatedEntity();

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
                        finalDiff.put(item.getName(), FormItem.VALUE_SEMANTICS.UNDEFINED);
                    else
                        finalDiff.put(item.getName(), val);
                }
            }
        }

        return finalDiff;
    }

    /**
     * This is what the entity looks like with the user's changes on the form.
     */
    
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

            boolean quoted = (object instanceof String);

            if(quoted)sb.append("\"");
            sb.append(object.toString());
            if(quoted)sb.append("\"");
        }

        return sb.toString();
    }

    
    public void clearValues() {

        for(Map<String, FormItem> groupItems : formItems.values())
        {
            for(FormItem item : groupItems.values())
            {
                item.resetMetaData();
                item.clearValue();
            }
        }

        editedEntity = null;

        refreshPlainView();
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
