package org.themarioga.game.cah.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.game.cah.dao.intf.RoundDao;
import org.themarioga.game.cah.enums.CardTypeEnum;
import org.themarioga.game.cah.models.*;
import org.themarioga.game.commons.dao.AbstractHibernateDao;

import java.util.List;

@Repository
public class RoundDaoImpl extends AbstractHibernateDao<Round> implements RoundDao {

    public RoundDaoImpl() {
        setClazz(Round.class);
    }

    @Override
    public PlayedCard getMostVotedCard(long roundId) {
        return getCurrentSession().createNativeQuery("SELECT * FROM t_round_playedcards WHERE round_id=:round_id and CARD_ID = (SELECT CARD_ID FROM (SELECT CARD_ID, count(card_id) as value_ocurrence FROM t_round_playervotes WHERE round_id=:round_id GROUP BY card_id ORDER BY value_ocurrence DESC LIMIT 1) as CIvo)", PlayedCard.class).setParameter("round_id", roundId).setMaxResults(1).getSingleResultOrNull();
    }

    @Override
    public List<DeckCard> getDeckCards(Game game, int cardNumber, CardTypeEnum cardTypeEnum) {
        return getCurrentSession().createQuery("SELECT g FROM DeckCard g INNER JOIN Card c on g.card = c WHERE g.game=:game and c.type=:type ORDER BY RAND()", DeckCard.class).setParameter("game", game).setParameter("type", cardTypeEnum).setMaxResults(cardNumber).getResultList();
    }

    @Override
    public long countPlayedCards(Round round) {
        return getCurrentSession().createQuery("SELECT count(p) FROM PlayedCard p WHERE p.round=:round", Long.class).setParameter("round", round).getSingleResultOrNull();
    }

    @Override
    public long countVotedCards(Round round) {
        return getCurrentSession().createQuery("SELECT count(v) FROM VotedCard v WHERE v.round=:round", Long.class).setParameter("round", round).getSingleResultOrNull();
    }

}
