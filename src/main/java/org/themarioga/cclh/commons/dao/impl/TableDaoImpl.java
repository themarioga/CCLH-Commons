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
    public PlayedCard getMostVotedCard(long gameId) {
        return getCurrentSession()
                .createNativeQuery("SELECT * FROM T_TABLE_PLAYEDCARDS WHERE CARD_ID = (SELECT CARD_ID FROM (SELECT CARD_ID, count(card_id) as value_ocurrence FROM T_TABLE_PLAYERVOTES WHERE game_id=:game_id GROUP BY card_id ORDER BY value_ocurrence DESC LIMIT 1))", PlayedCard.class).setParameter("game_id", gameId).getSingleResultOrNull();
    }

}
