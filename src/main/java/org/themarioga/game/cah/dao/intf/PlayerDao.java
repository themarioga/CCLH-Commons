package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.cah.models.Player;
import org.themarioga.game.cah.models.PlayedCard;
import org.themarioga.game.cah.models.VotedCard;
import org.themarioga.game.commons.dao.InterfaceHibernateDao;

public interface PlayerDao extends org.themarioga.game.commons.dao.intf.PlayerDao, InterfaceHibernateDao<Player> {

    PlayedCard findCardByPlayer(Long playerId);

    VotedCard findVotesByPlayer(Long playerId);

}
