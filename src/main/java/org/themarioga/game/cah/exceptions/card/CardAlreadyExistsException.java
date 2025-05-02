package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardAlreadyExistsException extends CAHApplicationException {

    public CardAlreadyExistsException() {
        super(CAHErrorEnum.CARD_ALREADY_EXISTS);
    }

}
