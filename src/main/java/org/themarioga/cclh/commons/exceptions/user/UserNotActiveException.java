package org.themarioga.cclh.commons.exceptions.user;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class UserNotActiveException extends ApplicationException {

    private final long id;

    public UserNotActiveException(long id) {
        super(ErrorEnum.USER_NOT_ACTIVE);

        this.id = id;
    }

    public long getID() {
        return id;
    }

}
