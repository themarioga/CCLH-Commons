package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyFilledException extends ApplicationException {

    public GameAlreadyFilledException() {
        super(ErrorEnum.GAME_ALREADY_FILLED);
    }

}
