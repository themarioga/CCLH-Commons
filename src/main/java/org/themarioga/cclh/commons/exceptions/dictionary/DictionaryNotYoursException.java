package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryNotYoursException extends ApplicationException {

    public DictionaryNotYoursException() {
        super(ErrorEnum.DICTIONARY_NOT_YOURS);
    }

}
