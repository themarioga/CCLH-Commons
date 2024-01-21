package org.themarioga.cclh.commons.exceptions.deck;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DeckMaxCollaboratorsReached extends ApplicationException {

    public DeckMaxCollaboratorsReached() {
        super(ErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
