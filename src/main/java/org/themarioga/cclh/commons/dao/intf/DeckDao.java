package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Deck;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DeckDao extends InterfaceHibernateDao<Deck> {

    List<Deck> getDictionariesPaginated(User creator, int firstResult, int maxResults);

}
