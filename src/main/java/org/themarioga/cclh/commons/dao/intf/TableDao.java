package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;

public interface TableDao extends InterfaceHibernateDao<Table> {

    PlayedCard getMostVotedCard(long gameId);

    long countPlayedCards(Game game);

    long countVotedCards(Game game);

}
