package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardDoesntExistsException extends CAHApplicationException {

    public CardDoesntExistsException() {
        super(CAHErrorEnum.CARD_NOT_FOUND);
    }

}
