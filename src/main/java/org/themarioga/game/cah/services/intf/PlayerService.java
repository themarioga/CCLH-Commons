package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.Card;
import org.themarioga.game.commons.models.User;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    Player create(Game game, User user);

    void delete(Player player);

    Player findById(UUID id);

    Player findByUser(User user);

    Player findByUserId(UUID userId);

    Player findPlayerByUserAndGame(User user, Game game);

    void insertWhiteCardsIntoPlayerHand(Player player, List<Card> cardsToTransfer);

    Player incrementPoints(Player player);

    Player removeCardFromHand(Player player, Card card);

}
