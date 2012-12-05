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
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import org.jboss.ballroom.client.spi.Framework;
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

        /**
         * actions that reveal content
         */
        addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                if(keyDownEvent.getNativeKeyCode()== KeyCodes.KEY_ENTER)
                {
                    revealContent(false);
                }
            }
        });

        addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent mouseDownEvent) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    public void execute() {
                        revealContent(true);
                    }
                });
            }
        });

        Scheduler.get().scheduleEntry(new Scheduler.ScheduledCommand() {
            public void execute() {
                framework.getEventBus().addHandler(LHSHighlightEvent.TYPE, LHSNavTree.this);
            }
        });


        /*
            Allow clicks on lhs titles to open thr tree
        */

        addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                EventTarget target = event.getNativeEvent().getEventTarget();
                com.google.gwt.dom.client.Element el = Element.as(target);
                final String title = el.getInnerText();

                if(el.getTagName().equalsIgnoreCase("div"))
                {
                    applyStateChange(new StateChange()
                    {
                        public void applyTo(TreeItem treeItem) {

                            boolean isMatched = title.equals(treeItem.getText());

                            if(isMatched)
                            {
                                treeItem.setState(!treeItem.getState());
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * flag the 'active' item and revel content if necessary
     */
    private void revealContent(boolean open) {

        TreeItem activeItem = getSelectedItem();

        if(activeItem instanceof LHSNavTreeItem)
        {
            ((LHSNavTreeItem)activeItem).reveal();
        }
    }

    public String getTreeId() {
        return treeId;
    }

    @Override
    public void addItem(TreeItem item) {
        item.getElement().setAttribute(TREE_ID_ATTRIBUTE, treeId);
        super.addItem(item);

    }

    public void onSelectedNavTree(final LHSHighlightEvent event) {

        if(category.equals(event.getCategory()) || event.getCategory().equals("*"))
        {
            applyStateChange(new StateChange()
            {
                public void applyTo(TreeItem treeItem) {


                    if(!(treeItem instanceof LHSNavTreeItem))
                        return;

                    LHSNavTreeItem target = (LHSNavTreeItem) treeItem;

                    String token = treeItem.getElement().hasAttribute("token") ?
                            treeItem.getElement().getAttribute("token") : "not-set";

                    boolean isSelected = event.getItem().equals(treeItem.getText())
                            || token.equals(event.getToken());

                    if(isSelected)
                    {

                        if(prevNavItem!=null && !prevNavItem.equals(treeItem))
                            prevNavItem.setActive(false);

                        prevNavItem=target;

                        openParents(treeItem);

                        target.setActive(true);

                    }
                    else
                    {
                        target.setActive(false);
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

        for(int x=0; x<item.getChildCount(); x++)
        {
            dfsItem(stateChange, item.getChild(x));
        }

        stateChange.applyTo(item);

    }

    interface StateChange {
        void applyTo(TreeItem item);
    }

    public void expandTopLevel() {
        for(int i=0; i<getItemCount(); i++)
        {
            TreeItem item = getItem(i);
            item.setState(true);
        }
    }


}
