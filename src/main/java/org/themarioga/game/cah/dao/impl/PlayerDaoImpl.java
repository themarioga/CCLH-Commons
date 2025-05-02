package org.themarioga.game.cah.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.game.cah.dao.intf.PlayerDao;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.PlayedCard;
import org.themarioga.game.cah.models.VotedCard;
import org.themarioga.game.commons.dao.AbstractHibernateDao;
import org.themarioga.game.commons.models.User;

@Repository
public class PlayerDaoImpl extends AbstractHibernateDao<Player> implements PlayerDao {

    public PlayerDaoImpl() {
        setClazz(Player.class);
    }

    @Override
    public Player findPlayerByUser(User user) {
        return getCurrentSession().createQuery("SELECT t FROM Player t where t.user=:user", Player.class).setParameter("user", user).getSingleResultOrNull();
    }

    @Override
    public PlayedCard findCardByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT pc FROM PlayedCard pc where pc.id=:player_id", PlayedCard.class).setParameter("player_id", playerId).getSingleResultOrNull();
    }

    @Override
    public VotedCard findVotesByPlayer(Long playerId) {
        return getCurrentSession().createQuery("SELECT pc FROM VotedCard pc where pc.id=:player_id", VotedCard.class).setParameter("player_id", playerId).getSingleResultOrNull();
    }

}
