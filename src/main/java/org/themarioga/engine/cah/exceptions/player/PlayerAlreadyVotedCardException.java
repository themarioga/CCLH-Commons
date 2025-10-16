package org.themarioga.engine.cah.exceptions.player;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class PlayerAlreadyVotedCardException extends CAHApplicationException {

    public PlayerAlreadyVotedCardException() {
        super(CAHErrorEnum.PLAYER_ALREADY_VOTED_CARD);
    }

}
