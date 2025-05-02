package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardAlreadyVotedException extends CAHApplicationException {

    public CardAlreadyVotedException() {
        super(CAHErrorEnum.CARD_ALREADY_VOTED);
    }

}
