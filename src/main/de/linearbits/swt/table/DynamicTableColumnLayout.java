/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 *******************************************************************************/
package de.linearbits.swt.table;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * 
 * DynamicTableColumnLayout
 * 
 * <p>Manages the length of a set of columns.</p>
 * 
 */
public class DynamicTableColumnLayout extends Layout {

    private final List<DynamicColumnData>             mcolumns           = new LinkedList<DynamicColumnData>();
    private final List<DynamicColumnData>             pcolumns           = new LinkedList<DynamicColumnData>();
    private final Map<TableColumn, DynamicColumnData> mapColumnData      = new HashMap<TableColumn, DynamicColumnData>();
    private final Map<Composite, Listener>            installedListeners = new HashMap<Composite, Listener>();
    private int                                       totalFixedWidth    = 0;
    private double                                    totalPerctWidth    = 0.0d;

    /**
     * Get column data
     * @param tableColumn TableColumn
     * @return DynamicColumnData
     */
    public DynamicColumnData getColumnData(final TableColumn tableColumn) {
        return this.mapColumnData.get(tableColumn);
    }

    /**
     * Redistribute percentage
     * @param tableColumn TableColumn
     * @param value Double
     */
    public void redistributePercentage(final TableColumn tableColumn, final Double value) {
        final int qtdCols = (this.pcolumns.size() - 1);
        for (final DynamicColumnData data : this.pcolumns) {
            final DynamicLength preferredLength = data.getPreferredLength();
            if (tableColumn != data.getTableColumn()) {
                preferredLength.setValue((preferredLength.getValue() + (value / qtdCols)));
            }
        }
    }

    /**
     * Set dynamic column data
     * @param dynamicColumnData DynamicColumnData
     */
    public void setColumnData(final DynamicColumnData dynamicColumnData) {
        this.mapColumnData.put(dynamicColumnData.getTableColumn(), dynamicColumnData);
        this.mcolumns.add(dynamicColumnData);
        if (dynamicColumnData.getPreferredLength().getMeasure() == DynamicLengthMeasure.PERCENTAGE) {
            this.pcolumns.add(dynamicColumnData);
        }
    }

    /**
     * Set column data
     * @param tableColumn TableColumn
     * @param preferredLength String
     */
    public void setColumnData(final TableColumn tableColumn, final String preferredLength) {
        setColumnData(new DynamicColumnData(tableColumn, preferredLength));
    }

    /**
     * Set column data
     * @param tableColumn TableColumn
     * @param preferredLength String
     * @param minLength String
     */
    public void setColumnData(final TableColumn tableColumn, final String preferredLength, final String minLength) {
        setColumnData(new DynamicColumnData(tableColumn, preferredLength, minLength));
    }

    /**
     * Add total length
     * @param dynamicLength DynamicLength
     * @return boolean
     */
    private boolean addTotalLength(final DynamicLength dynamicLength) {
        switch (dynamicLength.getMeasure()) {
        case PIXEL:
            this.totalFixedWidth += dynamicLength.getValue();
            break;
        case PERCENTAGE:
            this.totalPerctWidth += dynamicLength.getValue();
        }
        return (dynamicLength.getValue() != 0);
    }

    /**
     * Update column width
     * @param dynamicColumnData DynamicColumnData
     * @param width Double
     */
    private void updateColumnWidth(final DynamicColumnData dynamicColumnData, final Double width) {
        final TableColumn tableColumn = dynamicColumnData.getTableColumn();
        final TreeColumn treeColumn = dynamicColumnData.getTreeColumn();

        if (tableColumn != null) {
            tableColumn.setWidth(width.intValue());
        } else if (treeColumn != null) {
            treeColumn.setWidth(width.intValue());
        } else {
            throw new IllegalStateException("No valid to set the column width!");
        }
    }

    @Override
    protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
        if (this.installedListeners.get(composite) == null) {
            final Listener listener = new Listener() {
                @Override
                public void handleEvent(final Event event) {
                    composite.layout();
                }
            };
            composite.addListener(SWT.Resize, listener);
            this.installedListeners.put(composite, listener);
        }

        final Control child = composite.getChildren()[0];
        final Point childSize = child.computeSize(wHint, hHint);
        int width = Math.max(0, childSize.x);
        int height = Math.max(0, childSize.y);

        if (wHint != SWT.DEFAULT) {
            width = wHint;
        }

        if (hHint != SWT.DEFAULT) {
            height = hHint;
        }

        return new Point(width, height);
    }

    @Override
    protected void layout(final Composite composite, final boolean flushCache) {
        final Rectangle containerArea = composite.getClientArea();
        final Scrollable table = getControl(composite);
        table.setBounds(containerArea);
        final Rectangle tableArea = table.getClientArea();
        this.totalFixedWidth = 0;
        this.totalPerctWidth = 0;

        // Calculate totals
        for (final DynamicColumnData column : this.mcolumns) {
            if (column.isVisible()) {
                final DynamicLength prefLength = column.getPreferredLength();
                final DynamicLength minLength = column.getMinLength();

                if (prefLength.getValue() != 0) {
                    addTotalLength(prefLength);
                } else {
                    addTotalLength(minLength);
                }
            }
        }

        final Integer precentPixelWidth = (tableArea.width - this.totalFixedWidth);

        // Set widths
        for (final DynamicColumnData dynamicColumnData : this.mcolumns) {
            if (dynamicColumnData.isVisible()) {
                final DynamicLength prefLength = dynamicColumnData.getPreferredLength();
                final Double prefLengthValue = prefLength.getValue();
                final DynamicLengthMeasure prefLengthMeasure = prefLength.getMeasure();
                final DynamicLength minLength = dynamicColumnData.getMinLength();
                final Double minLengthValue = minLength.getValue();
                switch (prefLengthMeasure) {
                case PIXEL:
                    updateColumnWidth(dynamicColumnData, Math.max(prefLengthValue, minLengthValue));
                    break;
                case PERCENTAGE:
                    final double percentFromTotal = (prefLengthValue / this.totalPerctWidth);
                    final int width = (int) (percentFromTotal * precentPixelWidth);
                    updateColumnWidth(dynamicColumnData, Math.max(width, minLengthValue));
                }
            } else {
                updateColumnWidth(dynamicColumnData, 0d);
            }
        }
    }

    /**
     * Get control
     * @param composite Composite
     * @return Scrollable
     */
    Scrollable getControl(final Composite composite) {
        return ((Scrollable) composite.getChildren()[0]);
    }

}
