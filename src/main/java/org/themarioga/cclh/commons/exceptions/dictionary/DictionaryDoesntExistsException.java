package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryDoesntExistsException extends ApplicationException {

    public DictionaryDoesntExistsException() {
        super(ErrorEnum.DICTIONARY_NOT_FOUND);
    }

}
