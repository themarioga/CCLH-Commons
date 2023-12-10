package org.themarioga.cclh.commons.exceptions.card;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class CardAlreadyVotedException extends ApplicationException {

    public CardAlreadyVotedException() {
        super(ErrorEnum.CARD_ALREADY_VOTED);
    }

}
