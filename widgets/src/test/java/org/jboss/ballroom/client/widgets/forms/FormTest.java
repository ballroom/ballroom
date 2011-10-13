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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class FormTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.jboss.ballroom.Framework";
    }

    @Test
    public void testVisitItem() {
        Form<Integer> f = new Form<Integer>(Integer.class);
        FormItem<String> item = new TextBoxItem("hello", "Hello");
        FormItem<String> item2 = new TextBoxItem("helloThere", "Hello There");
        FormItem<String> item3 = new TextBoxItem("hello_7", "Hello");
        f.setFieldsInGroup("default", item, item2, item3);

        final List<FormItem<?>> visited = new ArrayList<FormItem<?>>();
        Form.FormItemVisitor visitor = new Form.FormItemVisitor() {
            @Override
            public void visit(FormItem item) {
                visited.add(item);
            }
        };
        f.visitItem("hello", visitor);
        assertEquals(2, visited.size());
        assertTrue(visited.contains(item));
        assertTrue(visited.contains(item3));

        visited.clear();
        f.visitItem("helloThere", visitor);
        assertEquals(1, visited.size());
        assertSame(item2, visited.get(0));
    }

    @Test
    public void testSetFields1() {
        FormItem<String> item = new TextBoxItem("1", "1");
        FormItem<String> item2 = new TextBoxItem("2", "2");
        FormItem<String> item3 = new TextBoxItem("3", "3");
        Form<Object> f = new Form<Object>(Object.class);
        f.setFields(item, item2, item3);

        assertEquals(Arrays.asList("1", "2", "3"), f.getFormItemNames());
    }

    @Test
    public void testSetFields2() {
        FormItem<String> item = new TextBoxItem("1", "1");
        FormItem<String> item2 = new TextBoxItem("2", "2");
        FormItem<String> item3 = new TextBoxItem("3", "3");
        Form<Object> f = new Form<Object>(Object.class);
        f.setFields(new FormItem[] {item, item2}, new FormItem [] {}, new FormItem[] {item3});

        assertEquals(Arrays.asList("1", "2", "3"), f.getFormItemNames());
    }

    @Test
    public void testSetFieldsInGroup1() {
        FormItem<String> item = new TextBoxItem("1", "1");
        FormItem<String> item2 = new TextBoxItem("2", "2");
        FormItem<String> item3 = new TextBoxItem("3", "3");
        Form<Object> f = new Form<Object>(Object.class);
        f.setFieldsInGroup("mygroup", item, item2, item3);

        assertEquals(Arrays.asList("1", "2", "3"), f.getFormItemNames());
    }

    @Test
    public void testSetFieldsInGroup2() {
        FormItem<String> item = new TextBoxItem("1", "1");
        FormItem<String> item2 = new TextBoxItem("2", "2");
        FormItem<String> item3 = new TextBoxItem("3", "3");
        Form<Object> f = new Form<Object>(Object.class);
        f.setFieldsInGroup("someGroup", new FormItem[] {item, item2}, new FormItem [] {}, new FormItem[] {item3});

        assertEquals(Arrays.asList("1", "2", "3"), f.getFormItemNames());
    }
}
