package org.themarioga.cclh.commons.exceptions;

import org.themarioga.cclh.commons.enums.ErrorEnum;

public class ApplicationException extends RuntimeException {

    private final ErrorEnum error;

    public ApplicationException(ErrorEnum error) {
        this.error = error;
    }

    public ErrorEnum getErrorEnum() {
        return error;
    }

    @Override
    public String getMessage() {
        return error.getErrorDesc();
    }
}
