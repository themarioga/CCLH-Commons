package org.themarioga.engine.cah.exceptions.player;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class PlayerCannotDrawCardException extends CAHApplicationException {

    public PlayerCannotDrawCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_DRAW_CARD);
    }

}
