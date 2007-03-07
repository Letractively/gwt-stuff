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

package org.mcarthur.sandy.gwt.table.client;

import com.google.gwt.user.client.Element;

/**
 * Opera specific behaviors.
 *
 * @author Sandy McArthur
 */
class ObjectListTableImplOpera extends ObjectListTableImpl {

    /*
     * 2006-02-27: Couldn't reproduce this problem in Opera 9.10
     *
     * Opera seems to need the thead/tfoot elements to be kept at the start/end of the table's
     * child elements. The ordering doesn't seem to matter in HTML source, only when updating
     * the DOM.
     */
    private void Xadd(final ObjectListTable olt, final ObjectListTable.ObjectListTableBodyGroup rowGroup, final ObjectListTable.ObjectListTableBodyGroup beforeGroup, final int beforeIndex) {
        Element beforeElement = null;
        if (beforeGroup != null) {
            beforeElement = beforeGroup.getElement();
            olt.getTbodies().add(beforeIndex, rowGroup);
        } else if (olt.getTfoot() != null) {
            beforeElement = olt.getTfoot().getElement();
            olt.getTbodies().add(rowGroup);
        }
        insertBefore(olt.getElement(), rowGroup.getElement(), beforeElement);
    }
}