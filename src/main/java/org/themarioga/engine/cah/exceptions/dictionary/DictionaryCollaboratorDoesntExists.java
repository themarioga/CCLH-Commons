package org.themarioga.engine.cah.exceptions.dictionary;

import org.themarioga.engine.cah.enums.CAHErrorEnum;
import org.themarioga.engine.cah.exceptions.CAHApplicationException;

public class DictionaryCollaboratorDoesntExists extends CAHApplicationException {

    public DictionaryCollaboratorDoesntExists() {
        super(CAHErrorEnum.DICTIONARY_COLLAB_NOT_FOUND);
    }

}
