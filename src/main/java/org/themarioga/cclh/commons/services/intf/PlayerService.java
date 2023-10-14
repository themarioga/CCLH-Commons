package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface PlayerService {

    Player create(Game game, User user);

    Player findOne(long id);

    void addCardsToPlayerDeck(Player player, List<Card> playerCards);

    void transferCardsFromPlayerDeckToPlayerHand(Player player);

}
