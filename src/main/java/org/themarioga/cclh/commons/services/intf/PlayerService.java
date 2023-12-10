package org.themarioga.cclh.commons.services.intf;

import jakarta.transaction.Transactional;
import org.themarioga.cclh.commons.exceptions.ApplicationException;
import org.themarioga.cclh.commons.models.Card;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface PlayerService {

    Player create(Game game, User user);

    void addCardsToPlayerDeck(Player player, List<Card> playerCards);

    void transferCardsFromPlayerDeckToPlayerHand(Player player);

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = ApplicationException.class)
    void removeCardFromHand(Player player, Card card);

    Player findByUser(User user);

    Player findByUserId(long id);

}
