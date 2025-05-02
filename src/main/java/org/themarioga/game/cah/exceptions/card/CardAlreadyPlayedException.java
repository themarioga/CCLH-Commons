package org.themarioga.game.cah.exceptions.card;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class CardAlreadyPlayedException extends CAHApplicationException {

    public CardAlreadyPlayedException() {
        super(CAHErrorEnum.CARD_ALREADY_PLAYED);
    }

}
