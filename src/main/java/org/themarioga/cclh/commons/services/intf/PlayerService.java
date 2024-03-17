package org.themarioga.cclh.commons.services.intf;

import org.themarioga.cclh.commons.models.*;

import java.util.List;

public interface PlayerService {

    Player create(Game game, long userId);

    void delete(Player player);

    void transferWhiteCardsFromGameDeckToPlayerHand(Player player, List<GameDeckCard> cardsToTransfer);

    void removeCardFromHand(Player player, Card card);

    void incrementPoints(Player player);

    Player findById(long id);

    Player findByUser(User user);

    Player findByUserId(long id);

}
