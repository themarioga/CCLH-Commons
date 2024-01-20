package org.themarioga.cclh.commons.exceptions;

import org.themarioga.cclh.commons.enums.ErrorEnum;

public class ApplicationException extends RuntimeException {

    private final ErrorEnum error;

    public ApplicationException(ErrorEnum error) {
        this.error = error;
    }

    public ApplicationException(String message) {
        super(message);
        this.error = null;
    }

    public ErrorEnum getErrorEnum() {
        return error;
    }

    @Override
    public String getMessage() {
        return error != null ? error.getErrorDesc() : super.getMessage();
    }
}
