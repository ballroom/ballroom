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
package org.jboss.ballroom.client.widgets.stack;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;
import java.util.Map;

/**
 * A DeckPanel that manages widgets by name instead of index.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2011 Red Hat Inc.
 */
public class NamedDeckPanel extends DeckPanel {
    
    private int widgetCount = 0;
    private Map<String, Integer> widgetMap = new HashMap<String, Integer>();
    private Map<Integer, String> intKeyedWidgetMap = new HashMap<Integer, String>();

    public synchronized void add(String name, Widget widget) {
        super.add(widget);
        Integer index = new Integer(widgetCount);
        widgetMap.put(name, index);
        intKeyedWidgetMap.put(index, name);
        widgetCount++;
    }
    
    @Override
    public synchronized boolean remove(Widget widget) {
        throw new UnsupportedOperationException("Not implemented yet.");
        
        /* Not implemented yet.  Need to do below and also re-map names and indexes
        boolean removed = super.remove(widget);
        if (!removed) return false;
        String widgetName = visibleWidgetName();
        Integer index = widgetMap.get(widgetName);
        widgetMap.remove(widgetName);
        intKeyedWidgetMap.remove(index);
        widgetCount--;
        return true; */
    }
    
    public void showWidget(String name) {
        Integer index = widgetMap.get(name);
        if (index == null) throw new IllegalArgumentException("Widget " + name + " not found.");
        super.showWidget(index.intValue());
    }
    
    public String visibleWidgetName() {
        Integer index = new Integer(super.getVisibleWidget());
        return intKeyedWidgetMap.get(index);
    }
    
    @Override
    public void add(Widget w) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(IsWidget w, int beforeIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showWidget(int index) {
        throw new UnsupportedOperationException();
    }

}
