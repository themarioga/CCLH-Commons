package org.themarioga.engine.cah.dao.impl.game;

import org.springframework.stereotype.Repository;
import org.themarioga.engine.cah.dao.intf.game.RoundDao;
import org.themarioga.engine.cah.models.dictionaries.Card;
import org.themarioga.engine.cah.models.game.PlayedCard;
import org.themarioga.engine.cah.models.game.Round;
import org.themarioga.engine.cah.models.game.VotedCard;
import org.themarioga.engine.commons.dao.AbstractHibernateDao;

@Repository
public class RoundDaoImpl extends AbstractHibernateDao<Round> implements RoundDao {

    public RoundDaoImpl() {
        setClazz(Round.class);
    }

    @Override
    public VotedCard getMostVotedCard(Round round) {
        return getCurrentSession().createQuery("SELECT v FROM VotedCard v WHERE v.round = :round GROUP BY v.card ORDER BY COUNT(v) DESC", VotedCard.class).setParameter("round", round).getSingleResultOrNull();
	}

	@Override
	public PlayedCard getPlayedCardByCard(Round round, Card card) {
		return getCurrentSession().createQuery("SELECT p FROM PlayedCard p WHERE p.round = :round AND p.card = :card", PlayedCard.class).setParameter("round", round).setParameter("card", card).getSingleResultOrNull();
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
