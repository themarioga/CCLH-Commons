package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryNotCompletedException extends ApplicationException {

    public DictionaryNotCompletedException() {
        super(ErrorEnum.DICTIONARY_NOT_FILLED);
    }

}
