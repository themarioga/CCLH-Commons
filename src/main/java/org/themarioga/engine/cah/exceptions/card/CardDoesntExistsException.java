package org.themarioga.engine.cah.exceptions.card;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class CardDoesntExistsException extends CAHApplicationException {

    public CardDoesntExistsException() {
        super(CAHErrorEnum.CARD_NOT_FOUND);
    }

}
