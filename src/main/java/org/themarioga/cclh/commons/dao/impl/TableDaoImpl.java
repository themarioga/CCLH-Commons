package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.TableDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

@Repository
public class TableDaoImpl extends AbstractHibernateDao<Table> implements TableDao {

    public TableDaoImpl() {
        setClazz(Table.class);
    }

    @Override
    public void transferCardsToTableDeck(Game game, CardTypeEnum cardTypeEnum) {
        getCurrentSession().createNativeQuery("INSERT INTO T_TABLE_DECK SELECT :game_id AS game_id, id as card_id FROM T_CARD WHERE dictionary_id=:dictionary_id AND type=:type").setParameter("game_id", game.getId()).setParameter("dictionary_id", game.getDictionary().getId()).setParameter("type", cardTypeEnum).executeUpdate();

        getCurrentSession().flush();

        List<TableDeckCard> tableDeckCardList = getCurrentSession().createQuery("SELECT t FROM TableDeckCard t WHERE t.table=:table", TableDeckCard.class).setParameter("table", game.getTable()).getResultList();

        game.getTable().getDeck().addAll(tableDeckCardList);
    }

    @Override
    public PlayedCard getMostVotedCard(Long gameId) {
        return getCurrentSession().createNativeQuery("SELECT t_table_playedcards.*, count(t_table_playervotes.*) as value_ocurrences FROM t_table_playervotes INNER JOIN t_table_playedcards on (t_table_playedcards.card_id = t_table_playervotes.card_id AND t_table_playedcards.game_id = t_table_playervotes.game_id) WHERE t_table_playervotes.game_id = ? GROUP BY t_table_playervotes.card_id ORDER BY value_ocurrences DESC LIMIT 1", PlayedCard.class).setParameter(1, gameId).getSingleResultOrNull();
    }

    @Override
    public TableDeckCard getTableDeckCard(long gameId, int cardNumber) {
        return getCurrentSession().createNativeQuery("SELECT * FROM T_TABLE_DECK WHERE game_id=:game_id ORDER BY RAND()", TableDeckCard.class).setParameter("game_id", gameId).setMaxResults(cardNumber).getSingleResultOrNull();
    }

}
