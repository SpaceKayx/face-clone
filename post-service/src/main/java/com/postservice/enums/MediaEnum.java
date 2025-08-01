package com.postservice.enums;

public enum MediaEnum {

    VIDEO("VIDEO"),
    IMAGE("IMAGE");

    private final String name;

    MediaEnum(String name) {
        this.name = name;
    }

    // Getter method for name
    public String getName() {
        return name;
    }
}
