package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.PlayerDao;
import org.themarioga.cclh.models.Game;
import org.themarioga.cclh.models.PlayedCard;
import org.themarioga.cclh.models.Player;
import org.themarioga.cclh.models.PlayerVote;

@Repository
public class PlayerDaoImpl extends AbstractHibernateDao<Player> implements PlayerDao {

    public PlayerDaoImpl() {
        setClazz(Player.class);
    }

    @Override
    public PlayedCard findCardByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT t FROM PlayedCard t where player.id=:player_id", PlayedCard.class).setParameter("player_id", playerId).getSingleResult();
    }

    @Override
    public PlayerVote findVotesByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT t FROM PlayerVote t where player.id=:player_id", PlayerVote.class).setParameter("player_id", playerId).getSingleResult();
    }

}
