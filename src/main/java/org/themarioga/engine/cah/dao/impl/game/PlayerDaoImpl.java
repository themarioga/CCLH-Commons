package org.themarioga.engine.cah.dao.impl.game;

import org.springframework.stereotype.Repository;
import org.themarioga.engine.cah.dao.intf.game.PlayerDao;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.game.PlayedCard;
import org.themarioga.engine.cah.models.game.VotedCard;
import org.themarioga.engine.commons.dao.AbstractHibernateDao;
import org.themarioga.engine.commons.models.Game;
import org.themarioga.engine.commons.models.User;

@Repository
public class PlayerDaoImpl extends AbstractHibernateDao<Player> implements PlayerDao {

    public PlayerDaoImpl() {
        setClazz(Player.class);
    }

    @Override
    public Player findPlayerByUser(User user) {
        return getCurrentSession().createQuery("SELECT p FROM Player p where p.user=:user", Player.class).setParameter("user", user).getSingleResultOrNull();
    }

    @Override
    public Player findPlayerByUserAndGame(User user, Game game) {
        return getCurrentSession().createQuery("SELECT p FROM Player p where p.user=:user and p.game=:game", Player.class).setParameter("user", user).setParameter("game", game).getSingleResultOrNull();
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
