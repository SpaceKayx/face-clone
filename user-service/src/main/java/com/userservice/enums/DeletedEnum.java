package com.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletedEnum {

    DELETED(1),
    NOT_DELETED(0);

    private final int value;
}
