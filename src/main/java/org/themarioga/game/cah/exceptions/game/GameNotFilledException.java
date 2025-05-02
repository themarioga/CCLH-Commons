package org.themarioga.game.cah.exceptions.game;

import org.themarioga.game.commons.enums.ErrorEnum;
import org.themarioga.game.commons.exceptions.ApplicationException;

public class GameNotFilledException extends ApplicationException {

    public GameNotFilledException() {
        super(ErrorEnum.GAME_NOT_FILLED);
    }

}
