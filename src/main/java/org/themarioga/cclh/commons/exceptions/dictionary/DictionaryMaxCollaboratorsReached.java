package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryMaxCollaboratorsReached extends ApplicationException {

    public DictionaryMaxCollaboratorsReached() {
        super(ErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
