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

import java.util.Arrays;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class UnitBoxItemTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.jboss.ballroom.Framework";
    }

    @Test
    public void testUnixBoxItem() {
        UnitBoxItem<String> ubi = new UnitBoxItem<String>("amount", "Amount", String.class);
        ubi.setUnitPropertyName("units");
        assertEquals("amount", ubi.getName());
        assertEquals("units", ubi.getUnitItem().getName());
        assertEquals("Amount", ubi.getTitle());

        ubi.textBox.setText("Hello");
        assertEquals("Hello", ubi.getValue());

        ubi.setValue("Bye");
        assertEquals("Bye", ubi.textBox.getText());

        ubi.clearValue();
        assertEquals("", ubi.textBox.getText());
        assertEquals("", ubi.getValue());

        ubi.setChoices(Arrays.asList("a", "b"), null);
        assertNull(ubi.getUnitItem().getValue());

        ubi.setChoices(Arrays.asList("pounds", "kilos"), "kilos");
        assertEquals("kilos", ubi.getUnitItem().getValue());
        assertEquals(2, ubi.unitBox.getItemCount());
        assertEquals("kilos", ubi.unitBox.getItemText(0));
        assertEquals("pounds", ubi.unitBox.getItemText(1));
        assertEquals("List box should be a drop-down type", 1, ubi.unitBox.getVisibleItemCount());

        ubi.unitBox.setSelectedIndex(1);
        assertEquals("pounds", ubi.getUnitItem().getValue());

        assertTrue(ubi.validate("Hi"));
        assertFalse(ubi.validate("Hi there"));
        assertFalse(ubi.validate(""));
        assertFalse(ubi.validate(" "));
    }

    @Test
    public void testWidget() {
        UnitBoxItem<String> ubi = new UnitBoxItem<String>("amount", "Amount", String.class);
        ubi.setUnitPropertyName("units");
        ComplexPanel widget = (ComplexPanel) ubi.asWidget();
        TextBox foundTextBox = null;
        ListBox foundListBox = null;
        for (int i=0; i < widget.getWidgetCount(); i++) {
            Widget child = widget.getWidget(i);
            if (child instanceof TextBox)
                foundTextBox = (TextBox) child;

            if (child instanceof ListBox)
                foundListBox = (ListBox) child;
        }

        assertNotNull(foundTextBox);
        assertNotNull(foundListBox);

        assertTrue(foundTextBox.isEnabled());
        assertTrue(foundTextBox.isEnabled());

        ubi.setEnabled(false);
        assertFalse(foundTextBox.isEnabled());
        assertFalse(foundTextBox.isEnabled());

        ubi.setEnabled(true);
        assertTrue(foundTextBox.isEnabled());
        assertTrue(foundTextBox.isEnabled());
    }

    @Test
    public void testLong() {
        UnitBoxItem<Long> ubi = new UnitBoxItem<Long>("amount", "Amount", Long.class);
        ubi.setUnitPropertyName("units");
        ubi.setValue(Long.MAX_VALUE);
        assertEquals("" + Long.MAX_VALUE, ubi.textBox.getText());
        ubi.textBox.setText("" + Long.MIN_VALUE);
        assertEquals(new Long(Long.MIN_VALUE), ubi.getValue());

        assertTrue(ubi.validate(0L));
        assertTrue(ubi.validate(2L));
        assertTrue(ubi.validate(Long.MIN_VALUE));
        assertTrue(ubi.validate(Long.MAX_VALUE));
    }

    @Test
    public void testInteger() {
        UnitBoxItem<Integer> ubi = new UnitBoxItem<Integer>("amount", "Amount", Integer.class);
        ubi.setUnitPropertyName("units");
        ubi.setValue(Integer.MAX_VALUE);
        assertEquals("" + Integer.MAX_VALUE, ubi.textBox.getText());
        ubi.textBox.setText("" + Integer.MIN_VALUE);
        assertEquals(new Integer(Integer.MIN_VALUE), ubi.getValue());

        assertTrue(ubi.validate(0));
        assertTrue(ubi.validate(2));
        assertTrue(ubi.validate(Integer.MIN_VALUE));
        assertTrue(ubi.validate(Integer.MAX_VALUE));
    }

    @Test
    public void testRendering() {
        UnitBoxItem<String> ubi = new UnitBoxItem<String>("amount", "Amount", String.class);
        ubi.setUnitPropertyName("units");
        assertTrue(ubi.render());
        assertFalse("The unit item should return false on its render method as its rendered as part of the main control",
            ubi.getUnitItem().render());
    }

    @Test
    public void testModification() {
        UnitBoxItem<String> ubi = new UnitBoxItem<String>("amount", "Amount", String.class);
        ubi.setUnitPropertyName("units");
        ubi.setChoices(Arrays.asList("x", "y", "z"), "y");
        assertFalse("Precondition", ubi.isModified());
        ubi.textValueChangeHandler.onValueChange(null);
        assertTrue(ubi.isModified());

        ubi.resetMetaData();
        assertFalse(ubi.isModified());

        ubi.unitValueChangeHandler.onChange(null);
        assertTrue(ubi.getUnitItem().isModified());
    }
}
