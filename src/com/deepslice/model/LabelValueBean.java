package com.deepslice.model;
/**
 * <p>Title: LabelValueBean</p>
 * <p>Description: </p>
 * <p>Copyright: (c) 2011</p>
 * <p>Company: Gordon Food Service</p>
 * @version 1.0
 */

public class LabelValueBean {
    /**
     * The value to be returned to the server.
     */
    protected String value = null;
    
    /**
     * Construct a new LabelValueBean with the specified values.
     *
     * @param label The label to be displayed to the user
     * @param value The value to be returned to the server
     */
    public LabelValueBean(String label, String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * The label to be displayed to the user.
     */
    protected String label = null;

    public String getLabel() {
        return (this.label);
    }

    public String getValue() {
        return (this.value);
    }

    /**
     * Return a string representation of this object.
     */
    public String toString() {
        return (this.label);
    }

}
