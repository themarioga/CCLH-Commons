package org.themarioga.game.cah.exceptions.player;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class PlayerCannotVoteCardException extends CAHApplicationException {

    public PlayerCannotVoteCardException() {
        super(CAHErrorEnum.PLAYER_CANNOT_VOTE_CARD);
    }

}
