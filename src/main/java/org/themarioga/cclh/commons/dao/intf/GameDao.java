package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

public interface GameDao extends InterfaceHibernateDao<Game> {

    void transferCardsToGameDeck(Game game);

    Game getByRoom(Room room);

    Game getByCreator(User creator);

    List<GameDeckCard> getGameDeckCards(long gameId, int cardNumber, CardTypeEnum cardTypeEnum);

    Long countByRoom(Room room);

    Long countByCreator(User creator);
}
