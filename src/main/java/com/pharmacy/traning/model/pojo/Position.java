package com.pharmacy.traning.model.pojo;

/**
 * @author Besarab Victor
 * The enum Position.
 */
public enum Position {
    /**
     * User position.
     */
    USER ("user"),
    /**
     * Admin position.
     */
    ADMIN ("admin");

    private final String value;

    Position(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    
}
