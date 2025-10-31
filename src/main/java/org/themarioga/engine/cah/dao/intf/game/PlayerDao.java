package org.themarioga.engine.cah.dao.intf.game;

import org.themarioga.engine.cah.models.game.Player;
import org.themarioga.engine.cah.models.game.PlayedCard;
import org.themarioga.engine.cah.models.game.VotedCard;
import org.themarioga.engine.commons.dao.InterfaceHibernateDao;

public interface PlayerDao extends org.themarioga.engine.commons.dao.intf.PlayerDao, InterfaceHibernateDao<Player> {

    PlayedCard findCardByPlayer(Long playerId);

    VotedCard findVotesByPlayer(Long playerId);

}
