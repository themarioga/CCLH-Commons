package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameNotYourException extends ApplicationException {

    public GameNotYourException() {
        super(ErrorEnum.USER_NOT_FOUND);
    }

}
