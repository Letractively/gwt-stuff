/*
 * Copyright 2006 Sandy McArthur, Jr.
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import org.mcarthur.sandy.gwt.event.list.client.EventList;
import org.mcarthur.sandy.gwt.event.list.client.EventLists;
import org.mcarthur.sandy.gwt.event.list.client.ListEvent;
import org.mcarthur.sandy.gwt.event.list.client.ListEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An event driven table that is backed by an {@link EventList}. Each Object in the list reprsents
 * one {@link TableRowGroup row group}.
 *
 * <h3>CSS Style Rules</h3>
 * <ul class="css">
 * <li>.gwtstuff-ObjectListTable { }</li>
 * <li>plus style classes by TableRowGroup, TableRow, TableCell, etc...</li>
 * </ul>

 * @author Sandy McArthur
 */
public class ObjectListTable extends Panel implements SourcesMouseEvents {

    private static ObjectListTableImpl impl;
    static {
        impl = (ObjectListTableImpl)GWT.create(ObjectListTableImpl.class);
        impl.init();
    }

    private TableHeaderGroup thead;
    private TableFooterGroup tfoot;
    private List/*<TableBodyGroup>*/ tbodies = new ArrayList();

    private final WidgetCollection widgets = new WidgetCollection(this);

    private final Renderer model;
    private final EventList objects;
    private final ListEventListener objectsListener = new ListTableListEventListener();

    private MouseListenerCollection mouseListeners = null;

    /**
     * Create a new ObjectListTable backed by an empty object list.
     *
     * @param renderer builds table rows for each object.
     * @see #getObjects()
     */
    public ObjectListTable(final Renderer renderer) {
        this(renderer, EventLists.eventList());
    }

    /**
     * Create a new ObjectListTable backed by an EventList.
     *
     * @param renderer converts objects into table rows.
     * @param objects the objects to be displayed by the table.
     */
    public ObjectListTable(final Renderer renderer, final EventList objects) {
        this.model = renderer;
        this.objects = objects;
        setElement(DOM.createTable());
        addStyleName("gwtstuff-ObjectListTable");

        objects.addListEventListener(objectsListener);

        if (objects.size() > 0) {
            // fake a list changed event to initialize the table rows.
            objectsListener.listChanged(new ListEvent(objects, ListEvent.ADDED, 0, objects.size()));
        }
    }

    public EventList getObjects() {
        return objects;
    }

    List/*<TableBodyGroup>*/ getTbodies() {
        return tbodies;
    }

    TableFooterGroup getTfoot() {
        //createTfoot();
        return tfoot;
    }

    TableHeaderGroup getThead() {
        //createThead();
        return thead;
    }

    private void createTfoot() {
        if (tfoot == null) {
            final ObjectListTableFooterGroup footerGroup = new ObjectListTableFooterGroup();
            model.renderFooter(footerGroup);
            attach(footerGroup); // TODO: Does this do the right thing?
        }
    }

    private void createThead() {
        if (thead == null) {
            final ObjectListTableHeaderGroup headerGroup = new ObjectListTableHeaderGroup();
            model.renderHeader(headerGroup);
            attach(headerGroup); // TODO: Does this do the right thing?
        }
    }

    /**
     * Converts objects into table rows.
     *
     * <p>
     * <b>Note:</b> Modifying the EventList backing this table from the renderer is not allowed and
     * can lead to undefined behavior.
     * </p>
     */
    public interface Renderer {

        /**
         * Create the table rows and cells for an object.
         *
         * @param obj      the object these rows as based on.
         * @param rowGroup the row group to be assoiciated with <code>obj</code>.
         */
        public void render(Object obj, TableBodyGroup rowGroup);

        /**
         * Create the table rows and cells for the table's header.
         * If you do not want to have a table header then simply do nothing to <code>headerGroup</code>.
         *
         * @param headerGroup the table header row group.
         */
        public void renderHeader(TableHeaderGroup headerGroup);

        /**
         * Create the table rows and cells for the table's footer.
         * If you do not want to have a table footer then simply do nothing to the <code>footerGroup</code>.
         *
         * @param footerGroup the table footer row group.
         */
        public void renderFooter(TableFooterGroup footerGroup);
    }

    /**
     * A renderer implementing this interface receives notification when TableRowGroups are
     * attached and detached to the browser's document. This provides a means to register and
     * unregister event listeners that affect the TableRowGroups state.
     *
     * <p>
     * <b>Note:</b> Adding or removing table rows or table cells from an attach or detach event
     * can lead to undefined behavior.
     * </p>
     */
    public interface AttachRenderer extends Renderer {

        /**
         * Invoked when a TableBodyGroup has been attached to the browser's document.
         *
         * @param obj the object this row group represents.
         * @param rowGroup the row group representing the object.
         * @see #onDetach(Object, TableBodyGroup)
         */
        public void onAttach(Object obj, TableBodyGroup rowGroup);

        /**
         * Invoked when a TableHeaderGroup has been attached to the browser's document.
         *
         * @param rowGroup the table's header row group.
         */
        public void onAttach(TableHeaderGroup rowGroup);

        /**
         * Invoked when a TableFooterGroup has been attached to the browser's document.
         * 
         * @param rowGroup the table's footer row group.
         */
        public void onAttach(TableFooterGroup rowGroup);

        /**
         * Invoked when a TableBodyGroup is detached from the browser's document.
         * 
         * @param obj the object this row group represents.
         * @param rowGroup the row group representing the object.
         * @see #onAttach(Object, TableBodyGroup)
         */
        public void onDetach(Object obj, TableBodyGroup rowGroup);

        /**
         * Invoked when a TableHeaderGroup is detached from the browser's document.
         *
         * @param rowGroup the table's header row group.
         */
        public void onDetach(TableHeaderGroup rowGroup);

        /**
         * Invoked when a TableFooterGroup is detached from the browser's document.
         *
         * @param rowGroup the table's footer row group.
         */
        public void onDetach(TableFooterGroup rowGroup);
    }

    /**
     * Gets an iterator for the contained widgets. This iterator is required to
     * implement {@link java.util.Iterator#remove()}.
     */
    public Iterator iterator() {
        return widgets.iterator();
    }

    /**
     * Removes a child widget.
     *
     * @param w the widget to be removed
     * @return <code>true</code> if the widget was present
     */
    public boolean remove(final Widget w) {
        final boolean removed = widgets.contains(w);
        if (removed) {
            widgets.remove(w);
        }
        return removed;
    }

    private void add(final ObjectListTableRowGroup rowGroup, final ObjectListTableRowGroup beforeGroup) {
        final int beforeIndex;
        if (beforeGroup != null) {
            beforeIndex = tbodies.indexOf(beforeGroup);
        } else {
            beforeIndex = -1;
        }
        add(rowGroup, beforeGroup, beforeIndex);
    }

    private void add(final ObjectListTableRowGroup rowGroup, final ObjectListTableRowGroup beforeGroup, final int beforeIndex) {
        impl.add(this, rowGroup, beforeGroup, beforeIndex);
        // TODO? add to widgets before or after impl.add?
        addWidgets(rowGroup);
        // 2007-02-26: GWTCompiler can optimize this out if the instanceof is first
        if (model instanceof AttachRenderer && isAttached()) {
            final AttachRenderer attachRenderer = (AttachRenderer)model;
            attachRenderer.onAttach(rowGroup.getObject(), rowGroup);
        }
    }

    private void addWidgets(final TableRowGroup rowGroup) {
        final Iterator iter = rowGroup.getRows().iterator();
        while (iter.hasNext()) {
            final TableRow tr = (TableRow)iter.next();
            final Iterator cells = tr.iterator();
            while (cells.hasNext()) {
                final Widget cell = (Widget)cells.next();
                if (!widgets.contains(cell)) {
                    widgets.add(cell);
                }
            }
        }
    }

    private void attach(final TableHeaderGroup headerGroup) {
        thead = headerGroup;
        impl.attach(this, headerGroup);
        addWidgets(headerGroup);
    }

    private void attach(final TableFooterGroup footerGroup) {
        tfoot = footerGroup;
        impl.attach(this, footerGroup);
        addWidgets(footerGroup);
    }

    private void detach(final ObjectListTableRowGroup rowGroup) {
        // 2007-02-26: GWTCompiler can optimize this out if the instanceof is first
        if (model instanceof AttachRenderer && isAttached()) {
            final AttachRenderer attachRenderer = (AttachRenderer)model;
            attachRenderer.onDetach(rowGroup.getObject(), rowGroup);
        }
        DOM.removeChild(getElement(), rowGroup.getElement());
        tbodies.remove(rowGroup);
    }

    private void remove(final ObjectListTableRowGroup rowGroup) {
        final Iterator rit = rowGroup.getRows().iterator();
        while (rit.hasNext()) {
            final TableRow tr = (TableRow)rit.next();
            final Iterator trit = tr.iterator();
            while (trit.hasNext()) {
                final TableCell tc = (TableCell)trit.next();
                disown(tc);
                widgets.remove(tc);
            }
        }
    }

    private class ListTableListEventListener implements ListEventListener {
        public void listChanged(final ListEvent listEvent) {
            if (listEvent.isAdded()) {
                for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                    final Object obj = objects.get(i);
                    final ObjectListTableRowGroup rowGroup = new ObjectListTableRowGroup(obj);
                    model.render(obj, rowGroup);
                    //add(rowGroup, i);
                    ObjectListTableRowGroup before = null;
                    if (i < tbodies.size()) {
                        before = (ObjectListTableRowGroup)tbodies.get(i);
                    }
                    add(rowGroup, before, i);
                }

            } else if (listEvent.isRemoved()) {
                for (int i = listEvent.getIndexEnd() - 1; i >= listEvent.getIndexStart(); i--) {
                    final ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(i);
                    detach(rowGroup);
                    remove(rowGroup);
                }

            } else if (listEvent.isChanged()) {
                if (true) { // unoptimized
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(i);

                        // test if really different
                        if (obj != rowGroup.getObject()) {
                            // XXX: reposition rows instead of just remove/add them

                            // remove old
                            detach(rowGroup);
                            remove(rowGroup);

                            // insert new
                            rowGroup = new ObjectListTableRowGroup(obj);
                            model.render(obj, rowGroup);

                            ObjectListTableRowGroup before = null;
                            if (i < tbodies.size()) {
                                before = (ObjectListTableRowGroup)tbodies.get(i);
                            }
                            add(rowGroup, before, i);
                        }

                    }
                } else { // untested
                    final Map rows = new HashMap(listEvent.getIndexEnd() - listEvent.getIndexStart());
                    int k = listEvent.getIndexStart();
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)tbodies.get(k);
                        rows.put(rowGroup.getObject(), rowGroup);
                        detach(rowGroup);
                    }
                    for (int i = listEvent.getIndexStart(); i < listEvent.getIndexEnd(); i++) {
                        final Object obj = objects.get(i);
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)rows.remove(obj);
                        if (rowGroup == null) {
                            rowGroup = new ObjectListTableRowGroup(obj);
                            model.render(obj, rowGroup);
                        }
                        ObjectListTableRowGroup before = null;
                        if (i < tbodies.size()) {
                            before = (ObjectListTableRowGroup)tbodies.get(i);
                        }
                        add(rowGroup, before, i);
                    }

                    Iterator keyIter = rows.keySet().iterator();
                    while (keyIter.hasNext()) {
                        Object key = keyIter.next();
                        ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)rows.get(key);
                        keyIter.remove();
                        remove(rowGroup);
                    }
                }
            }
        }
    }

    class ObjectListTableRowGroup extends TableBodyGroup {
        private final Object obj;

        ObjectListTableRowGroup(final Object obj) {
            this.obj = obj;
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableRowGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }

        public Object getObject() {
            return obj;
        }
    }

    private class ObjectListTableHeaderGroup extends TableHeaderGroup {
        ObjectListTableHeaderGroup() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableHeaderGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }
    }

    private class ObjectListTableFooterGroup extends TableFooterGroup {
        ObjectListTableFooterGroup() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableFooterGroup");
        }

        public TableRow newTableRow() {
            return new ObjectListTableRow();
        }

        public void add(final TableRow row) {
            if (row instanceof ObjectListTableRow) {
                super.add(row);
            } else {
                throw new IllegalArgumentException("TableRow instance must be acquired from newTableRow()");
            }
        }
    }

    private class ObjectListTableRow extends TableRow {

        public ObjectListTableRow() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableRow");
        }

        public void add(final TableCell cell) {
            if (cell instanceof ObjectListTableDataCell || cell instanceof ObjectListTableHeaderCell) {
                super.add(cell);
            } else {
                throw new IllegalArgumentException("TableCell must be provided by newTableDataCell() or newTableHeaderCell()");
            }
        }

        protected void adopt(final Widget w, final Element container) {
            ObjectListTable.this.adopt(w, container);
        }

        public TableDataCell newTableDataCell() {
            return new ObjectListTableDataCell();
        }

        public TableHeaderCell newTableHeaderCell() {
            return new ObjectListTableHeaderCell();
        }
    }

    private class ObjectListTableDataCell extends TableDataCell {
        public ObjectListTableDataCell() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableDataCell");
        }
    }

    private class ObjectListTableHeaderCell extends TableHeaderCell {
        public ObjectListTableHeaderCell() {
            addStyleName("gwtstuff-ObjectListTable-ObjectListTableHeaderCell");
        }
    }


    protected void onAttach() {
        // Create the header and footers if they haven't been created yet.
        createThead();
        createTfoot();

        super.onAttach();

        if (model instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)model;

            final TableHeaderGroup thead = getThead();
            assert thead != null;
            attachRenderer.onAttach(thead);

            final List tbodies = getTbodies();
            final Iterator iter=tbodies.iterator();
            while (iter.hasNext()) {
                final ObjectListTableRowGroup tbody = (ObjectListTableRowGroup)iter.next();
                attachRenderer.onAttach(tbody.getObject(), tbody);
            }

            final TableFooterGroup tfoot = getTfoot();
            assert tfoot != null;
            attachRenderer.onAttach(tfoot);
        }
    }

    protected void onDetach() {
        super.onDetach();

        if (model instanceof AttachRenderer) {
            final AttachRenderer attachRenderer = (AttachRenderer)model;

            final TableHeaderGroup thead = getThead();
            assert thead != null;
            attachRenderer.onDetach(thead);

            final List tbodies = getTbodies();
            final Iterator iter=tbodies.iterator();
            while (iter.hasNext()) {
                final ObjectListTableRowGroup tbody = (ObjectListTableRowGroup)iter.next();
                assert tbody != null;
                attachRenderer.onDetach(tbody.getObject(), tbody);
            }

            final TableFooterGroup tfoot = getTfoot();
            assert tfoot != null;
            attachRenderer.onDetach(tfoot);
        }
    }

    public void onBrowserEvent(final Event event) {
        super.onBrowserEvent(event);

        final Element tableElement = getElement();
        Element target = DOM.eventGetTarget(event);

        // if the event is on the table element don't search table row groups.
        if (!DOM.compare(tableElement, target)) {

            // find the parent of the event target that is a row group.
            Element targetParent = DOM.getParent(target);
            while (target != null && !DOM.compare(tableElement, targetParent)) {
                target = targetParent;
                targetParent = DOM.getParent(target);
            }

            // fire the onBrowserEvent for the row group that the event came from.
            if (DOM.compare(target, getThead().getElement())) {
                getThead().onBrowserEvent(event);

            } else if (DOM.compare(target, getTfoot().getElement())) {
                getTfoot().onBrowserEvent(event);

            } else {
                final Iterator iter = tbodies.iterator();
                while (iter.hasNext()) {
                    final ObjectListTableRowGroup rowGroup = (ObjectListTableRowGroup)iter.next();
                    if (DOM.compare(target, rowGroup.getElement())) {
                        rowGroup.onBrowserEvent(event);
                        break;
                    }
                }
            }
        }

        if (mouseListeners != null) {
            final int eventType = DOM.eventGetType(event);
            switch (eventType) {
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEMOVE:
                case Event.ONMOUSEOVER:
                case Event.ONMOUSEOUT: {
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                }
            }
        }
    }

    public void addMouseListener(final MouseListener listener) {
        if (mouseListeners == null) {
            mouseListeners = new MouseListenerCollection();
        }
        mouseListeners.add(listener);
    }

    public void removeMouseListener(final MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
            if (mouseListeners.isEmpty()) {
                mouseListeners = null;
            }
        }
    }
}
