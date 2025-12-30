package org.themarioga.engine.cah.exceptions.game;

import org.themarioga.engine.commons.enums.CommonErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;

public class GameNotFilledException extends ApplicationException {

    public GameNotFilledException() {
        super(CommonErrorEnum.GAME_NOT_FILLED);
    }

}
