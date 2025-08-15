package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.Card;

import java.util.List;

public interface PlayerService extends org.themarioga.game.commons.services.intf.PlayerService<Player, Game> {

    void insertWhiteCardsIntoPlayerHand(Player player, List<Card> cardsToTransfer);

    Player incrementPoints(Player player);

    Player removeCardFromHand(Player player, Card card);

}
