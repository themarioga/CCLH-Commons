package org.themarioga.engine.cah.services.intf.game;

import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.commons.models.User;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    Player create(Game game, User user);

    void delete(Player player);

    Player findById(UUID id);

    Player findByUser(User user);

    Player findByUserId(UUID userId);

    Player findPlayerByGameAndUser(Game game, User user);

    void insertWhiteCardsIntoPlayerHand(Player player, List<Card> cardsToTransfer);

    Player incrementPoints(Player player);

    Player removeCardFromHand(Player player, Card card);

}
