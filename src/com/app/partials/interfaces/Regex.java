package com.app.partials.interfaces;

public abstract interface Regex {
    String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8}$";

    String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    String NAME_PATTERN = "^[a-zA-Z\s]+$";

    String MOBILE_NO_PATTERN = "^[0-9]{10}$";

    String DOUBLE_VAL_PATTERN = "-?\\d+(\\.\\d+)?";

    String INTEGER_VAL_PATTERN = "-?\\d+";
}
