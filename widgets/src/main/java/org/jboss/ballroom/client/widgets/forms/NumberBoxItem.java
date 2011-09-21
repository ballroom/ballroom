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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 3/28/11
 */
public class NumberBoxItem extends FormItem<Number> {

    private TextBox textBox;
    private InputElementWrapper wrapper;

    public NumberBoxItem(String name, String title) {
        super(name, title);

        textBox = new TextBox();
        textBox.setName(name);
        textBox.setTitle(title);

        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                isModified = true;
            }
        });

        wrapper = new InputElementWrapper(textBox, this);

    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }

    @Override
    public Number getValue() {
        String value = textBox.getValue().equals("") ? "0" : textBox.getValue();
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        textBox.setValue(null);
    }

    @Override
    public void setValue(Number number) {
        if(number.longValue()>=0)
        {
            textBox.setValue(String.valueOf(number));
        }
    }

    @Override
    public void setEnabled(boolean b) {
        textBox.setEnabled(b);
    }

    @Override
    public void setErroneous(boolean b) {
        super.setErroneous(b);
        wrapper.setErroneous(b);
    }

    @Override
    public String getErrMessage() {
        return "Invalid numeric value";
    }

    @Override
    public boolean validate(Number value) {

        boolean outcome = true;
        boolean isEmpty = textBox.getValue().equals("");

        if(isRequired() && isEmpty)
        {
            outcome = false;
        }
        else if(!isEmpty)
        {
            try {
                long i = Long.parseLong(textBox.getValue());
                outcome = (i>=0);
            } catch (NumberFormatException e) {
                outcome = false;
            }
        }

        return outcome;
    }

    @Override
    public void clearValue() {
        this.textBox.setText("");
    }
}
