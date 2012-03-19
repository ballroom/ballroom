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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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
 * @see LHSTreeSection
 *
 * @author Heiko Braun
 * @date 3/24/11
 */
public class LHSNavTree extends Tree implements LHSHighlightEvent.NavItemSelectionHandler {

    private static final String TREE_ID_ATTRIBUTE = "treeid";
    private static final Framework framework = GWT.create(Framework.class);

    private String treeId;
    private String category;
    private LHSNavTreeItem prevNavItem;

    public LHSNavTree(final String category) {
        super(DefaultTreeResources.INSTANCE);

        this.treeId = "lhs-nav-tree_"+HTMLPanel.createUniqueId();
        this.category = category;

        addStyleName("stack-section");

        addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                if(keyDownEvent.getNativeKeyCode()== KeyCodes.KEY_ENTER)
                {
                    handleSelectedItem();
                }
            }
        });

        addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                handleSelectedItem();
            }
        });

        //remove kdb highlight on blur
        addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {

                TreeItem item = getSelectedItem();
                if (item != null) {
                    item.getElement().getFirstChildElement().addClassName("lostFocus");
                }

            }
        });

        // show kbd highlight on focus
        addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent focusEvent) {

                TreeItem item = getSelectedItem();
                if (item != null) {
                    item.getElement().getFirstChildElement().removeClassName("lostFocus");
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



    private void handleSelectedItem() {
        TreeItem selectedItem = getSelectedItem();
        if(selectedItem instanceof LHSNavTreeItem)
        {
            ((LHSNavTreeItem)selectedItem).activate();
        }
    }

    /*private void activate(final LHSNavTreeItem navItem) {

        // reveal view
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                String token = navItem.getElement().getAttribute("token");
                framework.getPlaceManager().revealPlaceHierarchy(
                        Places.fromString(token)
                );
            }
        });

    }  */

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

                    if(isSelected)
                    {
                        treeItem.setActive(true);

                        if(prevNavItem!=null)
                            prevNavItem.setActive(false);

                        prevNavItem=treeItem;

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

    public void expandTopLevel() {
        for(int i=0; i<getItemCount(); i++)
        {
            TreeItem item = getItem(i);
            item.setState(true);
        }
    }


}
