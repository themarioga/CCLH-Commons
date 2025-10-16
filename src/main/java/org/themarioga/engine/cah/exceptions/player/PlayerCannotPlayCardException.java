package org.themarioga.engine.cah.exceptions.player;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class PlayerCannotPlayCardException extends CAHApplicationException {

    public PlayerCannotPlayCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_PLAY_CARD);
    }

}
