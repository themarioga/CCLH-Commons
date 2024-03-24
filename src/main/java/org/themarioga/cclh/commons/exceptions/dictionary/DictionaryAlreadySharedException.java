package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryAlreadySharedException extends ApplicationException {

    public DictionaryAlreadySharedException() {
        super(ErrorEnum.DICTIONARY_ALREADY_FILLED);
    }

}
