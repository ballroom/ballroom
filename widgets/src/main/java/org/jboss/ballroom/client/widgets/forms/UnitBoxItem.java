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

import java.util.Collection;
import java.util.TreeSet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author David Bosschaert
 */
public class UnitBoxItem<T> extends FormItem<T> implements ChoiceItem<String> {
    private final Class<T> valueClass;
    final TextBox textBox;
    final ListBox unitBox;
    private String defaultUnit;
    ValueChangeHandler<String> textValueChangeHandler;
    private final HorizontalPanel wrapper;
    private final UnitFieldFormItem unitFieldFormItem;
    ChangeHandler unitValueChangeHandler;

    public UnitBoxItem(String name, String unitName, String title, Class<T> cls) {
        super(name, title);

        valueClass = cls;

        textBox = new TextBox();
        textBox.setName(name);
        textBox.setTitle(title);
        textValueChangeHandler = new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                isModified = true;
            }
        };
        textBox.addValueChangeHandler(textValueChangeHandler);

        unitBox = new ListBox();
        unitBox.setVisibleItemCount(1);
        unitBox.setName(unitName);
        unitFieldFormItem = new UnitFieldFormItem(unitName);

        unitValueChangeHandler = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                isModified = true;
            }
        };
        unitBox.addChangeHandler(unitValueChangeHandler);

        wrapper = new HorizontalPanel();
        wrapper.add(textBox);
        wrapper.add(unitBox);
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getValue() {
        String textVal = textBox.getValue();

        if (valueClass.equals(String.class))
            return (T) textVal;

        // must be numeric
        if (textVal == "")
            textVal = "0";

        try {
            if (valueClass.equals(Long.class))
                return (T) Long.valueOf(textVal);

            if (valueClass.equals(Integer.class))
                return (T) Integer.valueOf(textVal);

            throw new IllegalStateException("Datatype not supported by control: " + valueClass.getName());
        } catch (NumberFormatException e) {
            return (T) new Long(-1L);
        }
    }

    @Override
    public void setValue(T value) {
        textBox.setValue("" + value);
    }

    @Override
    public void setEnabled(boolean b) {
        textBox.setEnabled(b);
        unitBox.setEnabled(b);
    }

    @Override
    public boolean validate(T value) {
        if (valueClass.equals(Long.class) || valueClass.equals(Integer.class))
            // if it's a long or an int then we can just check that the value passed in is the same.
            return value.getClass().equals(valueClass);

        if (valueClass.equals(String.class)) {
            String strVal = (String) value;

            if (isRequired() && strVal.equals(""))
                return false;
            else
                return !strVal.contains(" ");
        }

        // can't validate this type
        return false;
    }

    @Override
    public void clearValue() {
        textBox.setText("");
    }

    public static interface UnitsCallback {
        String [] getUnits();
    }

    @Override
    public void setChoices(Collection<String> units, String defUnit) {
        TreeSet<String> sortedUnits = new TreeSet<String>(units);
        if (defUnit != null)
            if (!sortedUnits.contains(defUnit))
                sortedUnits.add(defUnit);

        defaultUnit = defUnit;

        unitBox.clear();
        for(String unit : sortedUnits) {
            unitBox.addItem(unit);
        }

        getUnitItem().setValue(defUnit);
    }

    public FormItem<String> getUnitItem() {
        return unitFieldFormItem;
    }

    private class UnitFieldFormItem extends FormItem<String> {
        public UnitFieldFormItem(String name) {
            super(name, name);
        }

        @Override
        public String getValue() {
            int idx = unitBox.getSelectedIndex();
            if (idx >= 0)
                return unitBox.getItemText(idx);
            return null;
        }

        @Override
        public void setValue(String value) {
            int idx = -1;
            for (int i=0; i < unitBox.getItemCount(); i++) {
                if (unitBox.getValue(i).equals(value)) {
                    idx = i;
                    break;
                }
            }

            if (idx == -1) {
                unitBox.setSelectedIndex(-1);
                return;
            }

            unitBox.setSelectedIndex(idx);
        }

        @Override
        public Widget asWidget() {
            // return an empty widget, as the widget is part of the containing class
            return new SimplePanel();
        }

        @Override
        public boolean render() {
            // The control is rendered as part of the containing class
            return false;
        }

        @Override
        public void setEnabled(boolean b) {
            // happens in the containing class
        }

        @Override
        public boolean validate(String value)
        {
            // Since its a selection the value is always valid
            return true;
        }

        @Override
        public void clearValue() {
            setValue(defaultUnit);
        }
    }
}
