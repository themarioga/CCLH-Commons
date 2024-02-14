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
    public VotedCard getMostVotedCard(long gameId) {
        return getCurrentSession().createNativeQuery("SELECT *, count(card_id) as value_ocurrence FROM T_TABLE_PLAYERVOTES " + "WHERE game_id=:gameId GROUP BY card_id ORDER BY value_ocurrence", VotedCard.class).setParameter("gameId", gameId).setMaxResults(1).getSingleResultOrNull();
    }

}
