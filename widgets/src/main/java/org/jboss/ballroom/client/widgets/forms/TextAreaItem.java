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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @author David Bosschaert
 */
public class TextAreaItem extends FormItem<String> {
    protected TextArea textArea;
    private InputElementWrapper wrapper;
    ValueChangeHandler<String> valueChangeHandler;

    public TextAreaItem(String name, String title) {
        super(name, title);

        textArea = new TextArea();
        textArea.setName(name);
        textArea.setTitle(title);
        valueChangeHandler = new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                setModified(true);
            }
        };
        textArea.addValueChangeHandler(valueChangeHandler);
        wrapper = new InputElementWrapper(textArea, this);
    }

    @Override
    public Widget asWidget() {
        return wrapper;
    }

    @Override
    public String getValue() {
        return textArea.getValue();
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        textArea.setValue(null);
    }

    @Override
    public void setValue(String value) {
        textArea.setValue(value);
    }


    @Override
    public void setEnabled(boolean b) {
        textArea.setEnabled(b);
    }

    @Override
    public void setErroneous(boolean b) {
        super.setErroneous(b);
        wrapper.setErroneous(b);
    }

    @Override
    public boolean validate(String value) {
        return !(isRequired() && value.trim().equals(""));
    }

    @Override
    public void clearValue() {
        textArea.setText("");
    }
}
