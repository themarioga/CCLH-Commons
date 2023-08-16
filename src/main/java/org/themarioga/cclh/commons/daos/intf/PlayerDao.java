package org.themarioga.cclh.commons.daos.intf;

import org.themarioga.cclh.commons.daos.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.PlayedCard;
import org.themarioga.cclh.commons.models.Player;
import org.themarioga.cclh.commons.models.PlayerVote;

public interface PlayerDao extends InterfaceHibernateDao<Player> {

    PlayedCard findCardByPlayer(Long playerId);

    PlayerVote findVotesByPlayer(Long playerId);

}
