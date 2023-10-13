package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryAlreadyExistsException extends ApplicationException {

    public DictionaryAlreadyExistsException() {
        super(ErrorEnum.DICTIONARY_ALREADY_EXISTS);
    }

}
