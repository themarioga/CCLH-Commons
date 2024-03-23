package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryCollaboratorDoesntExists extends ApplicationException {

    public DictionaryCollaboratorDoesntExists() {
        super(ErrorEnum.USER_NOT_FOUND);
    }

}
