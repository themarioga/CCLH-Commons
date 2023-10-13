package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.PlayedCard;

@Repository
public class GameDaoImpl extends AbstractHibernateDao<Game> implements GameDao {

    public GameDaoImpl() {
        setClazz(Game.class);
    }

    @Override
    public Game getByRoomId(long roomId) {
        return getCurrentSession().createQuery("SELECT t FROM Game t where t.room=:room_id", Game.class).setParameter("room_id", roomId).getSingleResultOrNull();
    }

}
