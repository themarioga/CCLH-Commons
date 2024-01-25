package org.themarioga.cclh.commons.exceptions.deck;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DeckNotCompletedException extends ApplicationException {

    public DeckNotCompletedException() {
        super(ErrorEnum.DECK_NOT_FILLED);
    }

}
