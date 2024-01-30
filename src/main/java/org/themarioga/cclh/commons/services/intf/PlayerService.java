package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface PlayerService {

    Player create(Game game, long userId);

    void addCardsToPlayerDeck(Player player, List<Card> playerCards);

    void transferCardsFromPlayerDeckToPlayerHand(Player player);

    void removeCardFromHand(Player player, Card card);

    Player findById(long id);

    Player findByUser(User user);

    Player findByUserId(long id);

}
