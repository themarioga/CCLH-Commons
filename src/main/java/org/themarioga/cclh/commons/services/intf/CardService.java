package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Deck;

import java.util.List;

public interface CardService {

    Card create(Card card);

    Card update(Card card);

    void delete(Card card);

    void deleteById(long id);

    Card findOne(long id);

    List<Card> findCardsByDeckIdAndType(Deck deck, CardTypeEnum cardTypeEnum);

    long countCardsByDeckIdAndType(Deck deck, CardTypeEnum cardTypeEnum);

}
