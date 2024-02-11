package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.*;

import java.util.List;

public interface PlayerService {

    Player create(Game game, long userId);

    void transferCardsFromGameDeckToPlayerHand(Player player);

    void removeCardFromHand(Player player, Card card);

    Player findById(long id);

    Player findByUser(User user);

    Player findByUserId(long id);

}
