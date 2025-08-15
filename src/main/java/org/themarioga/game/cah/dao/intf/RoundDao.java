package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.cah.models.*;
import org.themarioga.game.commons.dao.InterfaceHibernateDao;

public interface RoundDao extends InterfaceHibernateDao<Round> {

    PlayedCard getMostVotedCard(long roundId);

    long countPlayedCards(Round round);

    long countVotedCards(Round round);

}
