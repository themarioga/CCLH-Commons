package org.themarioga.cclh.commons.exceptions.user;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class UserNotActiveException extends ApplicationException {

    public UserNotActiveException() {
        super(ErrorEnum.USER_NOT_ACTIVE);
    }

}
