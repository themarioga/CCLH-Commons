package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardNotPlayedException extends CAHApplicationException {

    public CardNotPlayedException() {
        super(CAHErrorEnum.CARD_NOT_FOUND);
    }

}
