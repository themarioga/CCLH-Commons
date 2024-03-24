package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryAlreadyFilledException extends ApplicationException {

    public DictionaryAlreadyFilledException() {
        super(ErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
