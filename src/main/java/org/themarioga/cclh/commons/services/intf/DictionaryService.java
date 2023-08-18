package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;

import java.util.List;

public interface DictionaryService {

    Dictionary create(Dictionary dictionary);

    Dictionary update(Dictionary dictionary);

    void delete(Dictionary dictionary);

    void deleteById(long id);

    Dictionary findOne(long id);

    List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    long countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

}
