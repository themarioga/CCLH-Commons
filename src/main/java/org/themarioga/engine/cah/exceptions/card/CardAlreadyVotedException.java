package org.themarioga.engine.cah.exceptions.card;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class CardAlreadyVotedException extends CAHApplicationException {

    public CardAlreadyVotedException() {
        super(CAHErrorEnum.CARD_ALREADY_VOTED);
    }

}
