package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameDoesntExistsException extends ApplicationException {

    public GameDoesntExistsException() {
        super(ErrorEnum.GAME_NOT_FOUND);
    }

}
