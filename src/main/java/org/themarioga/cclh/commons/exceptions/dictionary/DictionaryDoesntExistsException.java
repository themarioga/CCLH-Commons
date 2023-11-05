package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryDoesntExistsException extends ApplicationException {

    private final Long dictionaryId;

    public DictionaryDoesntExistsException(Long dictionaryId) {
        super(ErrorEnum.DICTIONARY_NOT_FOUND);

        this.dictionaryId = dictionaryId;
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }
}
