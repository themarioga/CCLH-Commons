package org.themarioga.game.cah.dao.intf;

import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.commons.dao.InterfaceHibernateDao;

import java.util.List;

public interface RoundDao extends InterfaceHibernateDao<Round> {

    PlayedCard getMostVotedCard(long roundId);

    List<DeckCard> getDeckCards(Game game, int cardNumber, CardTypeEnum cardTypeEnum);

    long countPlayedCards(Round round);

    long countVotedCards(Round round);

}
