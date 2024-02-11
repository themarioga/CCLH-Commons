package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Table;
import org.themarioga.cclh.commons.models.TableDeckCard;

import java.util.List;

public interface TableDao extends InterfaceHibernateDao<Table> {

    void transferCardsToTableDeck(Game game, CardTypeEnum cardTypeEnum);

    PlayedCard getMostVotedCard(Long gameId);

	TableDeckCard getTableDeckCard(long gameId, int cardNumber);
}
