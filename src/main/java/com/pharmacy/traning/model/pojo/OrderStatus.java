package com.pharmacy.traning.model.pojo;

/**
 * @author Besarab Victor
 * The enum Order status.
 */
public enum OrderStatus {

    /**
     * Completed order status.
     */
    COMPLETED ("completed"),
    /**
     * The Not completed.
     */
    NOT_COMPLETED("not completed");

    private final String value;

    OrderStatus(String value) {
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
