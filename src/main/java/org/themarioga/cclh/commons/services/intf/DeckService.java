package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Deck;

public interface DeckService {

    Deck create(Deck deck);

    Deck update(Deck deck);

    void delete(Deck deck);

    void deleteById(long id);

    Deck findOne(long id);

    Deck getDefaultDictionary();
}
