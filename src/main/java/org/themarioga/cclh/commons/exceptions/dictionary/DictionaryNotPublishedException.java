package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryNotPublishedException extends ApplicationException {

    public DictionaryNotPublishedException() {
        super(ErrorEnum.DICTIONARY_NOT_FILLED);
    }

}
