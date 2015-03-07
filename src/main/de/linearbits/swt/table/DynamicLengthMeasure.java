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

/**
 * 
 * DynamicLengthMeasure
 * 
 */
public enum DynamicLengthMeasure {

    PIXEL("px"),
    PERCENTAGE("%");

    /**
     * Dynamic Length Measure
     * @param id String
     * @return LengthMeasure
     */
    public static DynamicLengthMeasure fromId(final String id) {
        for (final DynamicLengthMeasure measure : DynamicLengthMeasure.values()) {
            if (measure.getId().equals(id)) { return measure; }
        }
        return null;
    }

    private String id;

    /**
     * Dynamic Length measure
     * @param id String
     */
    private DynamicLengthMeasure(final String id) {
        this.id = id;
    }

    /**
     * Get id
     * @return String
     */
    public String getId() {
        return this.id;
    }

}
