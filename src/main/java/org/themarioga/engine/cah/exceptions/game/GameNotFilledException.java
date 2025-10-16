package org.themarioga.engine.cah.exceptions.game;

import org.themarioga.engine.commons.enums.ErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;

public class GameNotFilledException extends ApplicationException {

    public GameNotFilledException() {
        super(ErrorEnum.GAME_NOT_FILLED);
    }

}
