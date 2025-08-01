package com.userservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LockedEnum {

    LOCKED(1),
    NOT_LOCKED(0);

    private final int value;

}
