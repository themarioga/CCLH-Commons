package org.themarioga.engine.cah.dao.intf.game;

import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.PlayedCard;
import org.themarioga.engine.cah.models.game.Round;
import org.themarioga.engine.commons.dao.InterfaceHibernateDao;

public interface RoundDao extends InterfaceHibernateDao<Round> {

	Card getMostVotedCard(Round round);

    PlayedCard getPlayedCardByCard(Round round, Card card);

    long countPlayedCards(Round round);

    long countVotedCards(Round round);

}
