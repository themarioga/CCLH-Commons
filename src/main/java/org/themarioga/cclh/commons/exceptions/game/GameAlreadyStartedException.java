package org.themarioga.cclh.commons.exceptions.game;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class GameAlreadyStartedException extends ApplicationException {

    public GameAlreadyStartedException() {
        super(ErrorEnum.GAME_ALREADY_STARTED);
    }

}
