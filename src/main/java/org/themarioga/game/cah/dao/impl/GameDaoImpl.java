package org.themarioga.game.cah.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.game.commons.dao.AbstractHibernateDao;
import org.themarioga.game.cah.dao.intf.GameDao;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.commons.models.Room;
import org.themarioga.game.commons.models.User;

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

    @Override
    public void transferCardsFromDictionaryToDeck(Game game) {
        getCurrentSession().createQuery("INSERT INTO DeckCard(game, card) SELECT :game AS game, c FROM Card c WHERE dictionary=:dictionary").setParameter("game", game).setParameter("dictionary", game.getDictionary()).executeUpdate();
    }

}
