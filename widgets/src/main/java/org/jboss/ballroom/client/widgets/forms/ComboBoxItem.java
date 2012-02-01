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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collection;

/**
 * @author Heiko Braun
 * @date 2/21/11
 */
public class ComboBoxItem extends FormItem<String> {


    private ListBox comboBox;
    private boolean defaultToFirst;

    private InputElementWrapper wrapper;
    //private boolean postInit = false;

    public ComboBoxItem(String name, String title) {
        super(name, title);
        this.comboBox = new ListBox();

        this.comboBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                setModified(true);
                setUndefined("".equals(getSelectedValue()));
            }
        });

        this.wrapper = new InputElementWrapper(comboBox.asWidget(), this);
        wrapper.getElement().setAttribute("style", "width:100%");

    }

    @Override
    public String getValue() {
        return getSelectedValue();
    }

    private String getSelectedValue() {

        int selectedIndex = comboBox.getSelectedIndex();
        if(selectedIndex>=0)
            return comboBox.getValue(selectedIndex);
        else
            return "";
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        clearSelection();
        //postInit = false;
    }

    @Override
    public void setValue(String value) {

        clearSelection();

        for(int i=0; i< comboBox.getItemCount(); i++)
        {
            if(comboBox.getValue(i).equals(value))
            {
                selectItem(i);
                break;
            }
        }

        //postInit = true;
    }

    public void selectItem(int i) {
        setUndefined(comboBox.getValue(i).equals(""));
        comboBox.setItemSelected(i, true);
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }
    
    public void clearSelection() {
        this.comboBox.setSelectedIndex(0);
    }

    public void setDefaultToFirstOption(boolean b) {
        this.defaultToFirst = b;
    }

    public void setValueMap(String[] values) {

        comboBox.clear();

        //comboBox.clearSelection();
        if(values.length==0 || !values[0].isEmpty())
            comboBox.addItem("");

        for(String s : values)
        {
            comboBox.addItem(s);
        }

        if(defaultToFirst)
            selectItem(0);
    }

    public void setValueMap(Collection<String> values) {
        comboBox.clear();
        //comboBox.clearSelection();

        if(values.isEmpty() || !values.iterator().next().isEmpty())
            comboBox.addItem("");

        for(String s : values)
        {
            comboBox.addItem(s);
        }

        if(defaultToFirst)
            selectItem(0);
    }

    @Override
    public void setEnabled(boolean b) {
        comboBox.setEnabled(b);
    }

    @Override
    public String getErrMessage() {
        return "missing selection";
    }

    @Override
    public boolean validate(String value) {

        if(isRequired() && getSelectedValue().equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void setErroneous(boolean b) {
        super.setErroneous(b);
        wrapper.setErroneous(b);
    }

    @Override
    public void clearValue() {
        clearSelection();

        if(defaultToFirst && comboBox.getItemCount()>0)
            selectItem(0);
    }
    
   /* public void addValueChangeHandler(ValueChangeHandler<String> handler) {
        this.comboBox.addValueChangeHandler(handler);
    }*/
}
