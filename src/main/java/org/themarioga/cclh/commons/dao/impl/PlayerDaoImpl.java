package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.models.*;

import java.util.List;

@Repository
public class PlayerDaoImpl extends AbstractHibernateDao<Player> implements PlayerDao {

    public PlayerDaoImpl() {
        setClazz(Player.class);
    }

    @Override
    public Player findPlayerByUser(User user) {
        return getCurrentSession().createQuery("SELECT t FROM Player t where user=:user", Player.class).setParameter("user", user).getSingleResultOrNull();
    }

    @Override
    public PlayedCard findCardByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT t FROM PlayedCard t where player.id=:player_id", PlayedCard.class).setParameter("player_id", playerId).getSingleResultOrNull();
    }

    @Override
    public VotedCard findVotesByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT t FROM VotedCard t where player.id=:player_id", VotedCard.class).setParameter("player_id", playerId).getSingleResultOrNull();
    }

    @Override
    public List<GameDeckCard> getGameDeckCards(long gameId, int cardNumber) {
        return getCurrentSession().createNativeQuery("SELECT * FROM T_GAME_DECK WHERE game_id=:game_id ORDER BY RAND()", GameDeckCard.class).setParameter("game_id", gameId).setMaxResults(cardNumber).getResultList();
    }

}
