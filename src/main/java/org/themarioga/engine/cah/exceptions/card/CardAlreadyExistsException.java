package org.themarioga.engine.cah.exceptions.card;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class CardAlreadyExistsException extends CAHApplicationException {

    public CardAlreadyExistsException() {
        super(CAHErrorEnum.CARD_ALREADY_EXISTS);
    }

}
