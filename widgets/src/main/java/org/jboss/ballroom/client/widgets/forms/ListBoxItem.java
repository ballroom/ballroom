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
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author David Bosschaert
 */
public class ListBoxItem extends FormItem<String> implements ChoiceItem<String> {
    protected ListBox listBox;
    private HorizontalPanel wrapper;
    ChangeHandler valueChangeHandler;

    public ListBoxItem(String name, String title) {
        super(name, title);

        listBox = new ListBox();
        listBox.setName(name);
        listBox.setTitle(title);
        listBox.setVisibleItemCount(1);

        valueChangeHandler = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                isModified = true;
            }
        };
        listBox.addChangeHandler(valueChangeHandler);

        wrapper = new HorizontalPanel();
        wrapper.add(listBox);
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }

    public void setChoices(Collection<String> choices, String defaultChoice) {
        Set<String> sortedChoices = new TreeSet<String>(choices);

        int i = 0;
        int idx = -1;

        listBox.clear();
        for (String c : sortedChoices) {
            listBox.addItem(c);

            if (c.equals(defaultChoice))
                idx = i;

            i++;
        }

        if (idx >= 0)
            listBox.setSelectedIndex(idx);
        else
            listBox.setSelectedIndex(-1);
    }

    @Override
    public String getValue() {
        int idx = listBox.getSelectedIndex();
        if (idx < 0)
            return null;

        return listBox.getItemText(idx);
    }

    @Override
    public void setValue(String value) {
        if (value == null)
            throw new NullPointerException();

        for (int i=0; i < listBox.getItemCount(); i++) {
            if (value.equals(listBox.getItemText(i))) {
                listBox.setSelectedIndex(i);
                return;
            }
        }

        // Can't find the value
        throw new IllegalStateException("Unable to set value " + value + " on listbox " + getName());
    }

    @Override
    public void setEnabled(boolean b) {
        listBox.setEnabled(b);
    }

    @Override
    public boolean validate(String value) {
        return true;
    }

    @Override
    public void clearValue() {
        // We could implement this but not sure when it would be useful
    }
}
