package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyExistsException extends ApplicationException {

    public GameAlreadyExistsException() {
        super(ErrorEnum.GAME_ALREADY_EXISTS);
    }

}
