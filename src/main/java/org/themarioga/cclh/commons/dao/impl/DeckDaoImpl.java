package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.DeckDao;
import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@Repository
public class DeckDaoImpl extends AbstractHibernateDao<Deck> implements DeckDao {

    public DeckDaoImpl() {
        setClazz(Deck.class);
    }

    @Override
    public List<Deck> getDictionariesPaginated(User creator, int firstResult, int maxResults) {
        return getCurrentSession().createQuery("SELECT t FROM Deck t WHERE t.published=true and (t.shared=true or (t.shared=false and t.creator=:creator))", Deck.class).setParameter("user", creator).getResultList();
    }

}
