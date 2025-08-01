package com.postservice.enums;

public enum EmojiEnum {

    LIKE("LIKE"),
    LOVE("LOVE"),
    CARE("CARE"),
    WOW("WOW"),
    SAD("SAD"),
    ANGRY("ANGRY");

    private final String name;

    EmojiEnum(String name) {
        this.name = name;
    }

    // Getter method for name
    public String getName() {
        return name;
    }
}
