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

package org.jboss.ballroom.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import org.jboss.ballroom.client.spi.Framework;
import org.jboss.ballroom.client.util.Places;
import org.jboss.ballroom.client.widgets.icons.DefaultTreeResources;

/**
 *
 * A tree that's used as a navigation element on the left hand side.<br>
 * It's driven by a token attribute that's associated with the tree item.
 *
 * @see LHSNavTreeItem
 *
 * @author Heiko Braun
 * @date 3/24/11
 */
public class LHSNavTree extends Tree implements LHSHighlightEvent.NavItemSelectionHandler {

    private static final String TREE_ID_ATTRIBUTE = "treeid";
    private static final Framework framework = GWT.create(Framework.class);

    private String treeId;
    private String category;

    public LHSNavTree(final String category) {
        super(DefaultTreeResources.INSTANCE);

        this.treeId = "lhs-nav-tree_"+HTMLPanel.createUniqueId();
        this.category = category;

        addStyleName("stack-section");

        // TODO: this clashes with ARIA. it causes auto selection of links when using keyboard navigation
        addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent<TreeItem> event) {

                final TreeItem selectedItem = event.getSelectedItem();

                if (selectedItem.getElement().hasAttribute("token")) {
                    String token = selectedItem.getElement().getAttribute("token");
                    framework.getPlaceManager().revealPlaceHierarchy(
                            Places.fromString(token)
                    );

                }
            }
        });

         Scheduler.get().scheduleEntry(new Scheduler.ScheduledCommand() {
             @Override
             public void execute() {
                 framework.getEventBus().addHandler(LHSHighlightEvent.TYPE, LHSNavTree.this);
             }
         });
    }

    public String getTreeId() {
        return treeId;
    }

    @Override
    public void addItem(TreeItem item) {
        item.getElement().setAttribute(TREE_ID_ATTRIBUTE, treeId);
        super.addItem(item);

    }

    @Override
    public void onSelectedNavTree(final LHSHighlightEvent event) {

        if(category.equals(event.getCategory()) || event.getCategory().equals("*"))
        {
            applyStateChange(new StateChange()
            {
                @Override
                public void applyTo(LHSNavTreeItem treeItem) {

                    String token = treeItem.getElement().hasAttribute("token") ?
                        treeItem.getElement().getAttribute("token") : "not-set";

                    boolean isSelected = event.getItem().equals(treeItem.getText())
                            || token.equals(event.getToken());

                    treeItem.setSelected(isSelected);

                    if(isSelected)
                    {
                        openParents(treeItem);
                    }
                }
            });
        }
    }

    private void openParents(TreeItem treeItem) {
        TreeItem parentItem = treeItem.getParentItem();
        if(parentItem !=null)
        {
            parentItem.setState(true);
            openParents(parentItem);
        }

    }

    void applyStateChange(StateChange stateChange)
    {
        for(int i=0; i<getItemCount(); i++)
        {
            dfsItem(stateChange, getItem(i));
        }
    }

    private void dfsItem(StateChange stateChange, TreeItem item) {

        if(null==item) return;

        if(item instanceof LHSNavTreeItem)
        {
            LHSNavTreeItem navItem = (LHSNavTreeItem) item;
            stateChange.applyTo(navItem);
            return;
        }

        for(int x=0; x<item.getChildCount(); x++)
        {
            dfsItem(stateChange, item.getChild(x));
        }
    }

    interface StateChange {
        void applyTo(LHSNavTreeItem item);
    }
}
