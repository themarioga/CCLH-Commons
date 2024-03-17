package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.GameDao;
import org.themarioga.cclh.commons.enums.CardTypeEnum;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

@Repository
public class GameDaoImpl extends AbstractHibernateDao<Game> implements GameDao {

    public GameDaoImpl() {
        setClazz(Game.class);
    }

    @Override
    public void transferCardsToGameDeck(Game game) {
        getCurrentSession().createQuery("INSERT INTO GameDeckCard(game, card) SELECT :game AS game, c FROM Card c WHERE dictionary=:dictionary").setParameter("game", game).setParameter("dictionary", game.getDictionary()).executeUpdate();
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
    public List<GameDeckCard> getGameDeckCards(Game game, int cardNumber, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT g FROM GameDeckCard g INNER JOIN Card c on g.card = c WHERE g.game=:game and c.type=:type ORDER BY RAND()", GameDeckCard.class).setParameter("game", game).setParameter("type", cardTypeEnum).setMaxResults(cardNumber).getResultList();
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
