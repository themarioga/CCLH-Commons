package org.themarioga.game.cah.exceptions.player;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class PlayerAlreadyPlayedCardException extends CAHApplicationException {

    public PlayerAlreadyPlayedCardException() {
        super(CAHErrorEnum.PLAYER_ALREADY_PLAYED_CARD);
    }

}
