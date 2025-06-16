package com.app.partials.interfaces;

public interface Regex {
    final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8}$";

    final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    final String NAME_PATTERN = "^[a-zA-Z\s]+$";

    final String MOBILE_NO_PATTERN = "^[0-9]{10}$";

    final String DOUBLE_VAL_PATTERN = "-?\\d+(\\.\\d+)?";

    final String INTEGER_VAL_PATTERN = "-?\\d+";
}
