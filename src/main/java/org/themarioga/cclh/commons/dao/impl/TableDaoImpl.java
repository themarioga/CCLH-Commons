package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.TableDao;
import org.themarioga.cclh.commons.models.*;

@Repository
public class TableDaoImpl extends AbstractHibernateDao<Table> implements TableDao {

    public TableDaoImpl() {
        setClazz(Table.class);
    }

    @Override
    public PlayedCard getMostVotedCard(Long gameId) {
        return getCurrentSession().createNativeQuery("SELECT t_table_playedcards.*, count(t_table_playervotes.card_id) as value_ocurrences FROM t_table_playervotes INNER JOIN t_table_playedcards on (t_table_playedcards.card_id = t_table_playervotes.card_id AND t_table_playedcards.game_id = t_table_playervotes.game_id) WHERE t_table_playervotes.game_id = ? GROUP BY t_table_playervotes.card_id ORDER BY value_ocurrences DESC LIMIT 1", PlayedCard.class).setParameter(1, gameId).getSingleResultOrNull();
    }

}
