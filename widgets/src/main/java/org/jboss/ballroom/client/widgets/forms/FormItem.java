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
import com.google.gwt.user.client.ui.Widget;

/**
 * Form item abstraction.
 *
 * @author Heiko Braun
 * @date 2/21/11
 */
public abstract class FormItem<T> implements InputElement<T>, Comparable<String> {

    protected String name;
    protected String title;

    protected boolean isRequired = true;
    protected String errMessage = "Invalid input";

    private boolean isErroneous = false;
    private boolean isModified = false;
    private boolean isUndefined = true;
    //private boolean supportExpressions = false;

    protected String expressionValue = null;

    public FormItem(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public int compareTo(String s) {
        return s.compareTo(this.getName());
    }

    public void setExpressionValue(String expr) {
        this.expressionValue = expr;
    }

    public boolean isExpressionValue() {
        /*if(!doesSupportExpressions())  // TODO: optimize from meta data
            return false;
        */
        String exprValue = asExpressionValue();
        return exprValue !=null && exprValue.startsWith("${") && exprValue.endsWith("}");
    }

    // to support expressions override this method
    public String asExpressionValue() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setErroneous(boolean b) {
        this.isErroneous = b;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isErroneous() {
        return isErroneous;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public boolean render() {
        return true;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public boolean isUndefined() {
        return isUndefined;
    }

    public  void setUndefined(boolean undefined) {
        isUndefined = undefined;
    }

    public void resetMetaData() {
        setModified(false);
        setUndefined(true);
        setErroneous(false);
        setExpressionValue(null);
    }

    /*public boolean doesSupportExpressions() {
        return supportExpressions;
    }

    public void setSupportExpressions(boolean b) {
        this.supportExpressions = b;
    } */

    public abstract Widget asWidget();

    public abstract void setEnabled(boolean b);

    public abstract boolean validate(T value);

    public abstract void clearValue();

    public enum VALUE_SEMANTICS {UNDEFINED}

    Element getInputElement() {
        return null;
    }

    protected void toggleExpressionInput(Widget target, boolean flag)
    {
        if(flag)
        {
            target.getElement().addClassName("expression-input");
        }
        else
            target.getElement().removeClassName("expression-input");
    }

    public static boolean isExpressionScheme(String value)
    {
        return value.startsWith("${") && value.endsWith("}");
    }


}
