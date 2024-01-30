package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DeckService {

    Deck create(Deck deck);

    Deck update(Deck deck);

    void delete(Deck deck);

    Deck findOne(long id);

    List<Deck> getDeckPaginated(User creator, int firstResult, int maxResults);

    Long getDeckCount(User creator);

    Deck getDefaultDeck();
}
