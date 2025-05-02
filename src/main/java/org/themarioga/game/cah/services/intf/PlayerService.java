package org.themarioga.game.cah.services.intf;

import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.Card;
import org.themarioga.game.cah.models.DeckCard;

import java.util.List;

public interface PlayerService extends org.themarioga.game.commons.services.intf.PlayerService {

    void transferWhiteCardsFromGameDeckToPlayerHand(Player player, List<DeckCard> cardsToTransfer);

    void incrementPoints(Player player);

    void removeCardFromHand(Player player, Card card);

}
