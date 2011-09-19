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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import org.junit.Test;

/**
 * @author David Bosschaert
 */
public class DefaultGroupRendererTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.jboss.ballroom.Framework";
    }

    @Test
    public void testRender() {
        DefaultGroupRenderer renderer = new DefaultGroupRenderer();

        RenderMetaData rmd = new RenderMetaData();
        rmd.setNumColumns(1);

        Map<String, FormItem> groupItems = new HashMap<String, FormItem>();
        groupItems.put("foo", new TextBoxItem("foo", "Foo"));
        groupItems.put("bar", new TextBoxItem("bar", "Bar") {
            @Override
            public boolean render() {
                return false;
            }
        });
        groupItems.put("zoo", new TextBoxItem("zoo", "Zoo") {
            @Override
            public boolean render() {
                return true;
            }
        });

        Widget widget = renderer.render(rmd, "groupName", groupItems);
        HTMLPanel panel = (HTMLPanel) widget;
        String html = panel.getElement().getInnerHTML();

        assertTrue(html.contains("Foo:"));
        assertTrue(html.contains("Zoo:"));
        assertFalse("Bar should not be rendered", html.contains("Bar:"));
    }
}
