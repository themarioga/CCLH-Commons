package org.themarioga.game.cah.exceptions.player;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class PlayerCannotPlayCardException extends CAHApplicationException {

    public PlayerCannotPlayCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_PLAY_CARD);
    }

}
