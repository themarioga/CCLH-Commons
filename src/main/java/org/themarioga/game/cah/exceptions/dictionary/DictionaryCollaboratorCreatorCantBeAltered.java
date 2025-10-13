package org.themarioga.game.cah.exceptions.dictionary;

import org.themarioga.game.cah.enums.CAHErrorEnum;
import org.themarioga.game.cah.exceptions.CAHApplicationException;

public class DictionaryCollaboratorCreatorCantBeAltered extends CAHApplicationException {

    public DictionaryCollaboratorCreatorCantBeAltered() {
        super(CAHErrorEnum.DICTIONARY_COLLAB_NOT_FOUND);
    }

}
