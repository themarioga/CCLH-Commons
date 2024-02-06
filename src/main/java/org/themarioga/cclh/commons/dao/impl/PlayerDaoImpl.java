package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.PlayerDao;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.VotedCard;
import org.themarioga.cclh.commons.models.User;

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

}
