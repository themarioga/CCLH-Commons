package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Dictionary;

import java.util.List;

public interface CardService {

    Card create(Card card);

    Card update(Card card);

    void delete(Card card);

    void deleteById(long id);

    Card findOne(long id);

    List<Card> findCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

    long countCardsByDictionaryIdAndType(Dictionary dictionary, CardTypeEnum cardTypeEnum);

}
