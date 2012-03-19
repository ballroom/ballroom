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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 3/2/11
 */
public class CheckBoxItem extends FormItem<Boolean> {

    private CheckBox checkBox;

    public CheckBoxItem(String name, String title) {
        super(name, title);
        checkBox = new CheckBox();
        checkBox.setTabIndex(0);
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                setModified(true);
            }
        });
        setUndefined(false);
    }

    @Override
    public Element getInputElement() {
        return checkBox.getElement().getFirstChildElement();
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        setUndefined(false); // implicitly defined
        checkBox.setValue(false);
    }

    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    @Override
    public void setValue(Boolean value) {
        checkBox.setValue(value);
    }

    @Override
    public Widget asWidget() {
        return checkBox;
    }

    @Override
    public void setEnabled(boolean b) {
        checkBox.setEnabled(b);
    }

    @Override
    public boolean validate(Boolean value) {
        return true;
    }

    @Override
    public void clearValue() {
        setValue(false);
    }
}
