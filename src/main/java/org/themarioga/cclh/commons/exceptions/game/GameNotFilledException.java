package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameNotFilledException extends ApplicationException {

    public GameNotFilledException() {
        super(ErrorEnum.GAME_NOT_FILLED);
    }

}
