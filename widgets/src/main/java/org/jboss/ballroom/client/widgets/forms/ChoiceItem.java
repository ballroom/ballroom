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

import java.util.Collection;

/**
 * Interface used to populate the possible choices in widgets that can show them
 * such as list boxes and items with units.
 *
 * @author David Bosschaert
 */
public interface ChoiceItem<T> {
    /** Specify the choices for an entry
     *
     * @param choices The list of possible choices
     * @param defaultChoice The default choice to be selected, may be <tt>null</tt> if there is not default
     */
    void setChoices(Collection<T> choices, T defaultChoice);
}
