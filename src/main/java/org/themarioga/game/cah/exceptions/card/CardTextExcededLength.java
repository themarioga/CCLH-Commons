package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardTextExcededLength extends CAHApplicationException {

    public CardTextExcededLength() {
        super(CAHErrorEnum.CARD_TEXT_TOO_LONG);
    }

}
