package org.themarioga.cclh.commons.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.daos.AbstractHibernateDao;
import org.themarioga.cclh.commons.daos.intf.PlayerDao;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.PlayerVote;

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
