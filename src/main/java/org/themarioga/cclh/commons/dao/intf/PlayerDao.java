package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

public interface PlayerDao extends InterfaceHibernateDao<Player> {

    Player findPlayerByUser(User user);

    PlayedCard findCardByPlayer(Long playerId);

    VotedCard findVotesByPlayer(Long playerId);

    List<GameDeckCard> getGameDeckCards(long gameId, int cardNumber);

}
