package org.themarioga.engine.cah.exceptions.game;

import org.themarioga.engine.commons.enums.CommonErrorEnum;
import org.themarioga.engine.commons.exceptions.ApplicationException;

public class GameAlreadyFilledException extends ApplicationException {

    public GameAlreadyFilledException() {
        super(CommonErrorEnum.GAME_ALREADY_FILLED);
    }

}
