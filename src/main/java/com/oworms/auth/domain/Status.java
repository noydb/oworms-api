package com.oworms.auth.domain;

public enum Status {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Status getStatus(String arg) {
        if (null == arg) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        for (Status value : Status.values()) {
            if (arg.equalsIgnoreCase(value.getLabel())) {
                return value;
            }
        }

        throw new IllegalArgumentException("That status does not exist");
    }
}
