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

package org.jboss.ballroom.client.widgets.icons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Icons extends ClientBundle {

    public static final Icons INSTANCE =  GWT.create(Icons.class);

    @Source("stack_open.png")
    ImageResource stack_opened();

    @Source("stack_closed.gif")
    ImageResource stack_closed();

    @Source("inventory.png")
    ImageResource inventory();

    @Source("inventory_small.png")
    ImageResource inventory_small();

    @Source("add.png")
    ImageResource add();

    @Source("add_small.png")
    ImageResource add_small();

    @Source("remove.png")
    ImageResource remove();

    @Source("remove_small.png")
    ImageResource remove_small();

    @Source("user.png")
    ImageResource user();


    @Source("icn_info_blank.png")
    ImageResource info_blank();

    @Source("icn_info_blue.png")
    ImageResource info_blue();

    @Source("icn_info_orange.png")
    ImageResource info_orange();

    @Source("icn_info_red.png")
    ImageResource info_red();

    @Source("close.png")
    ImageResource close();

    @Source("close_white.png")
    ImageResource close_white();

    @Source("profile.png")
    ImageResource profile();

    @Source("server_group.png")
    ImageResource serverGroup();

    @Source("server.png")
    ImageResource server();

    @Source("server_start.png")
    ImageResource serverInstance();

    @Source("package.png")
    ImageResource deployment();


    @Source("status_negative.png")
    ImageResource status_bad();

    @Source("status_ok.png")
    ImageResource status_good();

    @Source("status_warn.png")
    ImageResource status_warn();

    @Source("blank.png")
    ImageResource status_none();


    @Source("exclamation.png")
    ImageResource exclamation();

    @Source("database.png")
    ImageResource database();

    @Source("blank.png")
    ImageResource noIcon();

    @Source("messaging.png")
    ImageResource messaging();

    @Source("help.png")
    ImageResource help();

    @Source("maximize.png")
    ImageResource maximize();

    @Source("window_white.png")
    ImageResource maximize_white();

    @Source("minimize.png")
    ImageResource minimize();

    @Source("resizer.png")
    ImageResource resize();

    @Source("comboBoxPicker_Over.png")
        ImageResource comboPicker_over();

    @Source("comboBoxPicker.png")
    ImageResource comboPicker();

    @Source("expr.png")
    ImageResource expression();

}
