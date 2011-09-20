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
import java.util.Collections;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class ListBoxItemTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.jboss.ballroom.Framework";
    }

    @Test
    public void testListBoxItem() {
        ListBoxItem lbi = new ListBoxItem("listbox", "List Box");
        assertEquals("listbox", lbi.getName());
        assertEquals("List Box", lbi.getTitle());

        HorizontalPanel panel = (HorizontalPanel) lbi.asWidget();
        ListBox listBox = findListBox(panel);
        assertEquals(0, listBox.getItemCount());

        lbi.setChoices(Arrays.asList("b", "a"), null);
        assertEquals(2, listBox.getItemCount());
        assertEquals("a", listBox.getItemText(0));
        assertEquals("b", listBox.getItemText(1));

        assertNull(lbi.getValue());
        try {
            lbi.setValue("c");
            fail("Should have thrown an exception as 'c' is not one of the listbox values");
        } catch (IllegalStateException ise) {
            // good
        }
        assertNull(lbi.getValue());

        lbi.setValue("b");
        assertEquals("b", lbi.getValue());

        listBox.setSelectedIndex(0);
        assertEquals("a", lbi.getValue());

        lbi.setChoices(Collections.singleton("d"), "d");
        assertEquals(1, listBox.getItemCount());
        assertEquals("d", listBox.getItemText(0));
        assertEquals("d", lbi.getValue());
    }

    @Test
    public void testListBoxItemEnablement() {
        ListBoxItem lbi = new ListBoxItem("listbox", "List Box");
        HorizontalPanel panel = (HorizontalPanel) lbi.asWidget();
        ListBox listBox = findListBox(panel);
        assertTrue(listBox.isEnabled());

        lbi.setEnabled(false);
        assertFalse(listBox.isEnabled());

        lbi.setEnabled(true);
        assertTrue(listBox.isEnabled());
    }

    @Test
    public void testModification() {
        ListBoxItem lbi = new ListBoxItem("listbox", "List Box");
        lbi.setChoices(Arrays.asList("And one", "And two"), "And one");
        assertFalse("Precondition", lbi.isModified());
        lbi.valueChangeHandler.onChange(null);
        assertTrue(lbi.isModified());
        lbi.resetMetaData();
        assertFalse(lbi.isModified());
    }

    private ListBox findListBox(ComplexPanel panel) {
        for (int i=0; i < panel.getWidgetCount(); i++) {
            Widget child = panel.getWidget(i);
            if (child instanceof ListBox)
                return (ListBox) child;
        }

        return null;
    }
}
