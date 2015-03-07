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
 * DynamicLength
 * 
 */
public class DynamicLength {

    private Double               value                = 0.0d;
    private DynamicLengthMeasure dynamicLengthMeasure = null;

    /**
     * Constructor
     * @param value Double
     * @param dynamicLengthMeasure DynamicLengthMeasure
     */
    public DynamicLength(final Double value, final DynamicLengthMeasure dynamicLengthMeasure) {
        this.value = value;
        this.dynamicLengthMeasure = dynamicLengthMeasure;
    }

    /**
     * Get measure
     * @return DynamicLengthMeasure
     */
    public DynamicLengthMeasure getMeasure() {
        return this.dynamicLengthMeasure;
    }

    /**
     * Get value
     * @return Double
     */
    public Double getValue() {
        return this.value;
    }

    /**
     * Set dynamic length measure
     * @param dynamicLengthMeasure DynamicLengthMeasure
     */
    public void setMeasure(final DynamicLengthMeasure dynamicLengthMeasure) {
        this.dynamicLengthMeasure = dynamicLengthMeasure;
    }

    /**
     * Set value
     * @param value Double
     */
    public void setValue(final Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if ((this.value == null) || (this.dynamicLengthMeasure == null)) { return ""; }
        return (this.value + "" + this.dynamicLengthMeasure.getId());
    }

}
