package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardAlreadyPlayedException extends ApplicationException {

    public CardAlreadyPlayedException() {
        super(ErrorEnum.CARD_ALREADY_PLAYED);
    }

}
