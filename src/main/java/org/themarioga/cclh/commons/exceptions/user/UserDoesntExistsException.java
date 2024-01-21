package org.themarioga.cclh.commons.exceptions.user;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class UserDoesntExistsException extends ApplicationException {

    public UserDoesntExistsException() {
        super(ErrorEnum.USER_NOT_FOUND);
    }

}
