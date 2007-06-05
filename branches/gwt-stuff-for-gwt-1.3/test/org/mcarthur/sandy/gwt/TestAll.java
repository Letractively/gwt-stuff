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

package org.mcarthur.sandy.gwt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run all GWT-Stuff unit tests.
 *
 * @author Sandy McArthur
 */
public class TestAll extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(org.mcarthur.sandy.gwt.event.list.TestAll.suite());
        suite.addTest(org.mcarthur.sandy.gwt.table.TestAll.suite());

        return suite;
    }
}
