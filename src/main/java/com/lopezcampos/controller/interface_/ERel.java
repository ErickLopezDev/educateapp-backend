package com.lopezcampos.controller.interface_;

public enum ERel {
    SELF("self"),
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    ALL("all");

    private final String value;

    ERel(String value) { this.value = value; }

    public String getValue() { return value; }
}
