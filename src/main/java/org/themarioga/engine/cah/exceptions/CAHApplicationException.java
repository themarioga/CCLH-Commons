package org.themarioga.engine.cah.exceptions;

import org.themarioga.engine.cah.enums.CAHErrorEnum;

public class CAHApplicationException extends RuntimeException {

    private final CAHErrorEnum error;

    public CAHApplicationException(CAHErrorEnum error) {
        this.error = error;
    }

    public CAHApplicationException(String message) {
        super(message);
        this.error = null;
    }

    public CAHErrorEnum getErrorEnum() {
        return error;
    }

    @Override
    public String getMessage() {
        return error != null ? error.getErrorDesc() : super.getMessage();
    }
}
