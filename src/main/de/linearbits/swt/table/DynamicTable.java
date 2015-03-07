/*******************************************************************************
 * Copyright (c) 2011 Luis Carlos Moreira da Costa.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Luis Carlos Moreira da Costa (tcljava at gmail dot com) - initial API and implementation
 * Fabian Prasser (fabian.prasser at gmail dot com) - extraction from Opal and extensions
 *******************************************************************************/
package de.linearbits.swt.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * DynamicTable
 * @author Luis Carlos Moreira da Costa 
 * @author Fabian Prasser
 */
public class DynamicTable extends Table {

    private Composite                parent       = null;
    private Composite                container    = null;
    private Menu                     menuOriginal = null;
    private Menu                     menuHeader   = null;
    private DynamicTableColumnLayout layout       = null;

    /**
     * Constructor
     * @param parent Composite
     * @param style int
     */
    public DynamicTable(final Composite parent, final int style) {
        super(new Composite(parent, SWT.NONE) {
            @Override
            public void reskin(final int flags) {
                super.reskin(flags);
            }
        }, style);

        // Init
        this.parent = parent;
        this.container = super.getParent();
        this.layout = new DynamicTableColumnLayout();
        this.container.setLayout(this.layout);
        this.menuHeader = new Menu(this.container.getShell(), SWT.POP_UP);
        
        // Show menus
        addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                updateHeaderMenu();
                setMenu(((isMouseOverHeader(event.x, event.y)) ? DynamicTable.this.menuHeader : DynamicTable.this.menuOriginal));
            }
        });

        // Dispose header menu
        addListener(SWT.Dispose, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                DynamicTable.this.menuHeader.dispose();
            }
        });
    }

    @Override
    public DynamicTableColumnLayout getLayout() {
        return (DynamicTableColumnLayout) this.container.getLayout();
    }

    @Override
    public Composite getParent() {
        return this.parent;
    }

    @Override
    public void layout() {
        this.container.layout();
    }

    @Override
    public void setLayout(final Layout layout) {
        throw new IllegalStateException();
    }

    @Override
    public void setLayoutData(final Object layoutData) {
        this.container.setLayoutData(layoutData);
    }

    @Override
    public void setMenu(Menu menu) {
        if (menu != menuHeader) {
            menuOriginal = menu;
        }
        super.setMenu(menu);
    }

    /**
     * Updates the header menu
     * @param menu
     */
    private void updateHeaderMenu() {
        
        // Dispose old
        for (MenuItem item : menuHeader.getItems()) {
            item.dispose();
        }
        
        // Create new
        for (final TableColumn column : this.getColumns()) {
            final DynamicColumnData data = layout.getColumnData(column);
            final MenuItem item = new MenuItem(menuHeader, SWT.CHECK);
            item.setText(column.getText());
            item.setSelection(data.isVisible());
            item.addListener(SWT.Selection, new Listener() {
                @Override
                public void handleEvent(final Event event) {
                    final boolean checked = item.getSelection();
                    data.setVisible(checked);
                    column.setResizable(checked);
                    layout();
                }
            });
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components.
    }

    /**
     * Verify is mouse over header
     * @param x int
     * @param y int
     * @return boolean
     */
    protected boolean isMouseOverHeader(final int x, final int y) {
        final Point pt = Display.getDefault().map(null, DynamicTable.this, new Point(x, y));
        final Rectangle clientArea = getClientArea();
        return (clientArea.y <= pt.y) && (pt.y < (clientArea.y + getHeaderHeight()));
    }
}
