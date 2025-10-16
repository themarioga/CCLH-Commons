package org.themarioga.engine.cah.dao.intf.game;

import org.themarioga.engine.cah.models.game.Game;
import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.game.PlayedCard;
import org.themarioga.engine.cah.models.game.VotedCard;
import org.themarioga.engine.commons.dao.InterfaceHibernateDao;
import org.themarioga.engine.commons.models.User;

public interface PlayerDao extends InterfaceHibernateDao<Player> {

    Player findPlayerByUser(User user);

    Player findPlayerByUserAndGame(User user, Game game);

    PlayedCard findCardByPlayer(Long playerId);

    VotedCard findVotesByPlayer(Long playerId);

}
