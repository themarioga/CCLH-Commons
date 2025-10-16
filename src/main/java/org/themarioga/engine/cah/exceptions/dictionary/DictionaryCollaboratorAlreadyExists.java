package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryCollaboratorAlreadyExists extends CAHApplicationException {

    public DictionaryCollaboratorAlreadyExists() {
        super(CAHErrorEnum.DICTIONARY_COLLAB_ALREADY_EXISTS);
    }

}
