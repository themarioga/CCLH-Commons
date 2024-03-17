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
        return getCurrentSession().createNativeQuery("SELECT * FROM t_table_playedcards WHERE game_id=:game_id and CARD_ID = (SELECT CARD_ID FROM (SELECT CARD_ID, count(card_id) as value_ocurrence FROM t_table_playervotes WHERE game_id=:game_id GROUP BY card_id ORDER BY value_ocurrence DESC LIMIT 1) as CIvo)", PlayedCard.class).setParameter("game_id", gameId).setMaxResults(1).getSingleResultOrNull();
    }

    @Override
    public long countPlayedCards(Game game) {
        return getCurrentSession().createQuery("SELECT count(p) FROM PlayedCard p WHERE p.table=:table", Long.class).setParameter("table", game.getTable()).getSingleResultOrNull();
    }

    @Override
    public long countVotedCards(Game game) {
        return getCurrentSession().createQuery("SELECT count(v) FROM VotedCard v WHERE v.table=:table", Long.class).setParameter("table", game.getTable()).getSingleResultOrNull();
    }

}
