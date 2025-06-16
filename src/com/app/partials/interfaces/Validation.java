package com.app.partials.interfaces;

public interface Validation extends Regex {

    public default boolean isNameValid(String name) {
        if (name.trim().isBlank()) {
            return false;
        }
        if (!name.trim().matches(NAME_PATTERN)) {
            return false;
        }
        return true;
    }
}
