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

import com.google.gwt.dom.client.Element;
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


    private ListBox listBox;
    private boolean defaultToFirst;

    private InputElementWrapper wrapper;
    //private boolean postInit = false;

    public ComboBoxItem(String name, String title) {
        super(name, title);
        this.listBox = new ListBox();
        this.listBox.setTabIndex(0);
        this.listBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                setModified(true);
                setUndefined("".equals(getSelectedValue()));
            }
        });

        this.wrapper = new InputElementWrapper(listBox.asWidget(), this);
        wrapper.getElement().setAttribute("style", "width:100%");

    }

    @Override
    public String getValue() {
        return getSelectedValue();
    }

    @Override
    public Element getInputElement() {
        return listBox.getElement();
    }

    private String getSelectedValue() {

        int selectedIndex = listBox.getSelectedIndex();
        if(selectedIndex>=0)
            return listBox.getValue(selectedIndex);
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

        for(int i=0; i< listBox.getItemCount(); i++)
        {
            if(listBox.getValue(i).equals(value))
            {
                selectItem(i);
                break;
            }
        }

        //postInit = true;
    }

    public void selectItem(int i) {
        setUndefined(listBox.getValue(i).equals(""));
        listBox.setItemSelected(i, true);
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }
    
    public void clearSelection() {
        this.listBox.setSelectedIndex(0);
    }

    public void setDefaultToFirstOption(boolean b) {
        this.defaultToFirst = b;
    }

    public void setValueMap(String[] values) {

        listBox.clear();

        //listBox.clearSelection();
        if(values.length==0 || !values[0].isEmpty())
            listBox.addItem("");

        for(String s : values)
        {
            listBox.addItem(s);
        }

        if(defaultToFirst)
            selectItem(0);
    }

    public void setValueMap(Collection<String> values) {
        listBox.clear();
        //listBox.clearSelection();

        if(values.isEmpty() || !values.iterator().next().isEmpty())
            listBox.addItem("");

        for(String s : values)
        {
            listBox.addItem(s);
        }

        if(defaultToFirst)
            selectItem(0);
    }

    @Override
    public void setEnabled(boolean b) {
        listBox.setEnabled(b);
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

        if(defaultToFirst && listBox.getItemCount()>0)
            selectItem(0);
    }
    
   /* public void addValueChangeHandler(ValueChangeHandler<String> handler) {
        this.listBox.addValueChangeHandler(handler);
    }*/
}
