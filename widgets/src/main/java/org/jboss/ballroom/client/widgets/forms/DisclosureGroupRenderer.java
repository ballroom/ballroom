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

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Map;

/**
 * @author Heiko Braun
 * @date 3/3/11
 */
public class DisclosureGroupRenderer  implements GroupRenderer {

    // link one to each other
    private DisclosurePanel first = null;

    @Override
    public Widget render(RenderMetaData metaData, String groupName, Map<String, FormItem> groupItems) {

        first = new DisclosurePanel(groupName);
        first.addStyleName("default-disclosure");
        first.addStyleName("fill-layout-width");

        DefaultGroupRenderer renderer = new DefaultGroupRenderer();

        first.add(
                renderer.render(metaData, groupName, groupItems)
        );

        return first;
    }

    @Override
    public Widget renderPlain(String groupName, PlainFormView plainView) {

        if(null==first)
            throw new IllegalStateException("Make sure to render the default (edit) widget first!");

        DisclosurePanel second = new DisclosurePanel(groupName);
        second.addStyleName("default-disclosure");
        second.addStyleName("fill-layout-width");
        second.add( plainView.asWidget());

        linkOneToEachOther(first, second);

        return second;
    }

    private void linkOneToEachOther(final DisclosurePanel one, final DisclosurePanel theOther) {
        theOther.addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                one.setOpen(true);
            }
        });

        theOther.addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> disclosurePanelCloseEvent) {
                one.setOpen(false);
            }
        });

        one.addOpenHandler(new OpenHandler<DisclosurePanel>() {
            @Override
            public void onOpen(OpenEvent<DisclosurePanel> event) {
                theOther.setOpen(true);
            }
        });

        one.addCloseHandler(new CloseHandler<DisclosurePanel>() {
            @Override
            public void onClose(CloseEvent<DisclosurePanel> disclosurePanelCloseEvent) {
                theOther.setOpen(false);
            }
        });

    }
}
