package org.themarioga.cclh.commons.exceptions.user;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class UserAlreadyExistsException extends ApplicationException {

    private final long id;

    public UserAlreadyExistsException(long id) {
        super(ErrorEnum.USER_ALREADY_EXISTS);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
