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

package org.mcarthur.sandy.gwt.event.list.test;

import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link org.mcarthur.sandy.gwt.event.list.client.TransformedEventList}.
 *
 * @author Sandy McArthur
 */
public abstract class TransformedEventListTest extends EventListTest {
    protected abstract EventList createBackedEventList(final EventList el);

    public void testNewTransformedEventListContainsAllOfDeeperListsElements() {
        final EventList el = EventLists.eventList();
        prefillWithIntegers(el, 5);

        final EventList bel = createBackedEventList(el);

        final List l = new ArrayList();
        prefillWithIntegers(l, 5);

        assertEquals(l, bel);
    }

    public void testAdd() {
        super.testAdd();

        final EventList el = EventLists.eventList();
        final EventList bel = createBackedEventList(el);

        ListEventListener lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(bel, ListEvent.ADDED, 0), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        bel.addListEventListener(lel);
        el.add("one");
        lel.listChanged(null);
        bel.removeListEventListener(lel);
        
        lel = new ListEventListener() {
            private int count = 0;
            public void listChanged(final ListEvent listEvent) {
                switch (count++) {
                    case 0:
                        assertEquals(new ListEvent(bel, ListEvent.ADDED, 1), listEvent);
                        break;
                    case 1:
                        assertNull(listEvent);
                        break;
                    default:
                        fail("Unexpected: " + listEvent);
                }
            }
        };
        bel.addListEventListener(lel);
        bel.add("two");
        lel.listChanged(null);
        bel.removeListEventListener(lel);


        assertTrue(bel.contains("one"));
        assertTrue(el.contains("two"));
    }
}
