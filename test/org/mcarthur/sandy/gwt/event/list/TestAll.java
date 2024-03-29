/*
 * Copyright 2007 Sandy McArthur, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mcarthur.sandy.gwt.event.list;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mcarthur.sandy.gwt.event.list.test.FilteredEventListTest;
import org.mcarthur.sandy.gwt.event.list.test.ListEventTest;
import org.mcarthur.sandy.gwt.event.list.test.RangedEventListTest;
import org.mcarthur.sandy.gwt.event.list.test.ReverseEventListTest;
import org.mcarthur.sandy.gwt.event.list.test.SortedEventListTest;
import org.mcarthur.sandy.gwt.event.list.test.SteadyRangedEventListTest;
import org.mcarthur.sandy.gwt.event.list.test.WrappedEventListTest;

/**
 * Run all GWT-Stuff Event List unit tests.
 *
 * @author Sandy McArthur
 */
public class TestAll extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ListEventTest.class);

        suite.addTestSuite(FilteredEventListTest.class);
        suite.addTestSuite(RangedEventListTest.class);
        suite.addTestSuite(ReverseEventListTest.class);
        suite.addTestSuite(SteadyRangedEventListTest.class);
        suite.addTestSuite(SortedEventListTest.class);
        suite.addTestSuite(WrappedEventListTest.class);

        suite.addTest(org.mcarthur.sandy.gwt.event.list.property.TestAll.suite());

        return suite;
    }
}
