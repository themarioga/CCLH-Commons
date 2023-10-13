package org.themarioga.cclh.commons.exceptions.user;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class UserDoesntExistsException extends ApplicationException {

    private final long id;

    public UserDoesntExistsException(long id) {
        super(ErrorEnum.USER_NOT_FOUND);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
