package org.themarioga.cclh.daos.intf;

import org.themarioga.cclh.daos.InterfaceHibernateDao;
import org.themarioga.cclh.models.PlayedCard;
import org.themarioga.cclh.models.Player;
import org.themarioga.cclh.models.PlayerVote;

public interface PlayerDao extends InterfaceHibernateDao<Player> {

    PlayedCard findCardByPlayer(Long playerId);

    PlayerVote findVotesByPlayer(Long playerId);

}
