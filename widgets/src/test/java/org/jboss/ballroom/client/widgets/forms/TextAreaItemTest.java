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
import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class TextAreaItemTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.jboss.ballroom.Framework";
    }

    @Test
    public void testTextAreaItem() {
        TextAreaItem tai = new TextAreaItem("a", "b");
        Assert.assertEquals("a", tai.getName());
        Assert.assertEquals("b", tai.getTitle());

        tai.textArea.setText("ho\nho");
        Assert.assertEquals("ho\nho", tai.getValue());

        tai.setValue("hi");
        Assert.assertEquals("hi", tai.getValue());

        tai.clearValue();
        Assert.assertEquals("", tai.getValue());

        tai.setEnabled(false);
        Assert.assertFalse(tai.textArea.isEnabled());
        tai.setEnabled(true);
        Assert.assertTrue(tai.textArea.isEnabled());
    }

    @Test
    public void testErroneous() {
        TextAreaItem tai = new TextAreaItem("a", "b");
        Assert.assertFalse("Precondition", tai.isErroneous());
        tai.setErroneous(true);
        Assert.assertTrue(tai.isErroneous());
        tai.resetMetaData();
        Assert.assertFalse(tai.isErroneous());
    }

    @Test
    public void testModification() {
        TextAreaItem tai = new TextAreaItem("a", "b");
        Assert.assertFalse("Precondition", tai.isModified());
        ValueChangeEvent<String> event = new ValueChangeEvent<String>("Some value") {};
        tai.valueChangeHandler.onValueChange(event);
        Assert.assertTrue(tai.isModified());
        tai.resetMetaData();
        Assert.assertFalse(tai.isModified());
    }

    @Test
    public void testValidate1() {
        TextAreaItem tai = new TextAreaItem("a", "b");
        tai.setRequired(true);
        Assert.assertFalse(tai.validate(""));
        Assert.assertTrue(tai.validate("hi ho"));
        Assert.assertTrue(tai.validate("hi\nho"));
    }

    @Test
    public void testValidate2() {
        TextAreaItem tai = new TextAreaItem("a", "b");
        tai.setRequired(false);
        Assert.assertTrue(tai.validate(""));
        Assert.assertTrue(tai.validate("hi ho"));
        Assert.assertTrue(tai.validate("hi\nho"));
    }
}
