package org.themarioga.game.cah.exceptions.player;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class PlayerAlreadyVotedCardException extends CAHApplicationException {

    public PlayerAlreadyVotedCardException() {
        super(CAHErrorEnum.PLAYER_ALREADY_VOTED_CARD);
    }

}
