package org.themarioga.game.cah.exceptions.player;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class PlayerCannotDrawCardException extends CAHApplicationException {

    public PlayerCannotDrawCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_DRAW_CARD);
    }

}
