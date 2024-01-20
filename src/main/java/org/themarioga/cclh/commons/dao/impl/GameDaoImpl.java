package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.models.Game;
import org.themarioga.cclh.commons.models.Room;
import org.themarioga.cclh.commons.models.User;

@Repository
public class GameDaoImpl extends AbstractHibernateDao<Game> implements GameDao {

    public GameDaoImpl() {
        setClazz(Game.class);
    }

    @Override
    public Game getByRoom(Room room) {
        return getCurrentSession().createQuery("SELECT t FROM Game t where t.room=:room", Game.class).setParameter("room", room).getSingleResultOrNull();
    }

    @Override
    public Game getByCreator(User creator) {
        return getCurrentSession().createQuery("SELECT t FROM Game t where t.creator=:creator", Game.class).setParameter("creator", creator).getSingleResultOrNull();
    }

    @Override
    public Long countByRoom(Room room) {
        return getCurrentSession().createQuery("SELECT count(t) FROM Game t where t.room=:room", Long.class).setParameter("room", room).getSingleResultOrNull();
    }

    @Override
    public Long countByCreator(User creator) {
        return getCurrentSession().createQuery("SELECT count(t) FROM Game t where t.creator=:creator", Long.class).setParameter("creator", creator).getSingleResultOrNull();
    }

}
