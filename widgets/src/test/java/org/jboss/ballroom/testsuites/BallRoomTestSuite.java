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
package org.jboss.ballroom.testsuites;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ballroom.client.widgets.forms.DefaultGroupRendererTest;
import org.jboss.ballroom.client.widgets.forms.FormTest;
import org.jboss.ballroom.client.widgets.forms.ListBoxItemTest;
import org.jboss.ballroom.client.widgets.forms.TextAreaItemTest;
import org.jboss.ballroom.client.widgets.forms.UnitBoxItemTest;

/**
 * The test suites exist in a separate package to avoid errors coming from GWT when running them
 * See: http://code.google.com/p/google-web-toolkit/issues/detail?id=2486
 *
 * Using a Test Suite greatly speeds up the running of the tests
 *
 * @author David Bosschaert
 */
public class BallRoomTestSuite extends GWTTestSuite {
    public static Test suite() {
        TestSuite suite= new TestSuite();
        suite.addTestSuite(DefaultGroupRendererTest.class);
        suite.addTestSuite(FormTest.class);
        suite.addTestSuite(ListBoxItemTest.class);
        suite.addTestSuite(TextAreaItemTest.class);
        suite.addTestSuite(UnitBoxItemTest.class);
        return suite;
    }
}

