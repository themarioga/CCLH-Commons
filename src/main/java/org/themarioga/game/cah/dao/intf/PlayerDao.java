package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.cah.models.Game;
import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.PlayedCard;
import org.themarioga.game.cah.models.VotedCard;
import org.themarioga.game.commons.dao.InterfaceHibernateDao;
import org.themarioga.game.commons.models.User;

public interface PlayerDao extends InterfaceHibernateDao<Player> {

    Player findPlayerByUser(User user);

    Player findPlayerByUserAndGame(User user, Game game);

    PlayedCard findCardByPlayer(Long playerId);

    VotedCard findVotesByPlayer(Long playerId);

}
