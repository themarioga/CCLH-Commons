package org.themarioga.engine.cah.exceptions.player;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class PlayerAlreadyPlayedCardException extends CAHApplicationException {

    public PlayerAlreadyPlayedCardException() {
        super(CAHErrorEnum.PLAYER_ALREADY_PLAYED_CARD);
    }

}
