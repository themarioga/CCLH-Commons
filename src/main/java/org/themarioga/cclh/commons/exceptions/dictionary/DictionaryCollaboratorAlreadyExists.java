package org.themarioga.cclh.commons.exceptions.dictionary;

import org.themarioga.cclh.commons.enums.ErrorEnum;
import org.themarioga.cclh.commons.exceptions.ApplicationException;

public class DictionaryCollaboratorAlreadyExists extends ApplicationException {

    public DictionaryCollaboratorAlreadyExists() {
        super(ErrorEnum.USER_ALREADY_EXISTS);
    }

}
