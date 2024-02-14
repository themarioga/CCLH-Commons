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
        getCurrentSession().createNativeQuery("INSERT INTO T_GAME_DECK SELECT :game_id AS game_id, id as card_id FROM T_CARD WHERE dictionary_id=:dictionary_id").setParameter("game_id", game.getId()).setParameter("dictionary_id", game.getDictionary().getId()).executeUpdate();
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
    public List<GameDeckCard> getGameDeckCards(long gameId, int cardNumber, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createNativeQuery("SELECT T_GAME_DECK.* FROM T_GAME_DECK INNER JOIN T_CARD on T_GAME_DECK.CARD_ID = T_CARD.ID WHERE game_id=:game_id and type=:type ORDER BY RAND()", GameDeckCard.class).setParameter("game_id", gameId).setParameter("type", cardTypeEnum).setMaxResults(cardNumber).getResultList();
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
