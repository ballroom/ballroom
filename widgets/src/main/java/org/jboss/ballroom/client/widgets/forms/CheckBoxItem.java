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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Heiko Braun
 * @date 3/2/11
 */
public class CheckBoxItem extends FormItem<Boolean> {

    private CheckBox checkBox;
    private final TextBox textBox;
    private final HorizontalPanel panel;
    private final InputElementWrapper wrapper;

    public CheckBoxItem(String name, String title) {
        super(name, title);
        checkBox = new CheckBox();
        checkBox.setTitle("Shift click for expression input");
        checkBox.setTabIndex(0);
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                setModified(true);
            }
        });
        setUndefined(false);

        checkBox.addClickHandler(new  ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (clickEvent.isShiftKeyDown()) {
                    toogleTextInput();
                    clickEvent.preventDefault();

                }
            }
        });

        textBox = new TextBox();
        textBox.setTitle("Shift click for regular input");
        textBox.addClickHandler(new  ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (clickEvent.isShiftKeyDown()) {
                    toogleBooleanInput();
                    clickEvent.preventDefault();
                }
            }
        });

        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                setModified(true);
                setUndefined(event.getValue().equals(""));
            }
        });

        wrapper = new InputElementWrapper(textBox, this);
        wrapper.setExpression(true);

        panel = new HorizontalPanel();
        panel.add(checkBox);
        panel.add(wrapper);

        wrapper.setVisible(false);

    }

    private void toogleTextInput() {
        wrapper.setVisible(true);
        checkBox.setVisible(false);
    }

    private void toogleBooleanInput() {
        wrapper.setVisible(false);
        checkBox.setVisible(true);
    }

    public Element getInputElement() {
        return checkBox.getElement().getFirstChildElement();
    }

    @Override
    public void resetMetaData() {
        super.resetMetaData();
        setUndefined(false); // implicitly defined
        checkBox.setValue(false);
        textBox.setText("");
    }

    @Override
    public String asExpressionValue() {
        String expr = wrapper.isVisible() ? textBox.getValue() : null;
        return expr;
    }

    @Override
    public Boolean getValue() {
        return checkBox.getValue();
    }

    @Override
    public void setExpressionValue(String expr) {
        textBox.setText(expr);
        toogleTextInput();
    }

    @Override
    public void setValue(Boolean value) {
        textBox.setText("");
        checkBox.setValue(value);
        toogleBooleanInput();
    }

    @Override
    public void setErroneous(boolean b) {
        wrapper.setErroneous(b);
    }

    @Override
    public String getErrMessage() {
        return "Invalid expression value";
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setEnabled(boolean b) {
        checkBox.setEnabled(b);
        textBox.setEnabled(b);
    }

    @Override
    public boolean validate(Boolean value) {
        boolean isValid = true;

        if(wrapper.isVisible())
        {
            // expression mode
            String text = textBox.getText();
            isValid = text !=null  && text.startsWith("${") && text.endsWith("}");
        }

        return isValid;
    }

    @Override
    public void clearValue() {
        setValue(false);
    }
}
