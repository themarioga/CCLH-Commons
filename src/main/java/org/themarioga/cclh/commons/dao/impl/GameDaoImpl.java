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
    public void transferCardsToGameDeck(Game game, CardTypeEnum cardTypeEnum) {
        getCurrentSession().createNativeQuery("INSERT INTO T_GAME_DECK SELECT :game_id AS game_id, id as card_id FROM T_CARD WHERE dictionary_id=:dictionary_id AND type=:type").setParameter("game_id", game.getId()).setParameter("dictionary_id", game.getDictionary().getId()).setParameter("type", cardTypeEnum).executeUpdate();

        getCurrentSession().flush();

        List<GameDeckCard> gameDeckCardList = getCurrentSession().createQuery("SELECT t FROM GameDeckCard t WHERE t.game=:game", GameDeckCard.class).setParameter("game", game).getResultList();

        game.getDeckCards().addAll(gameDeckCardList);
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
    public VotedCard getMostVotedCard(long gameId) {
        return getCurrentSession().createNativeQuery("SELECT *, count(card_id) as value_ocurrence FROM T_TABLE_PLAYERVOTES " + "WHERE game_id=:gameId GROUP BY card_id ORDER BY value_ocurrence", VotedCard.class).setParameter("gameId", gameId).setMaxResults(1).getSingleResultOrNull();
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
