package org.themarioga.engine.cah.exceptions.game;

import org.themarioga.engine.commons.enums.ErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;

public class GameAlreadyFilledException extends ApplicationException {

    public GameAlreadyFilledException() {
        super(ErrorEnum.GAME_ALREADY_FILLED);
    }

}
