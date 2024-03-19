package org.themarioga.cclh.commons.dao.intf;

import org.themarioga.cclh.commons.dao.InterfaceHibernateDao;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

public interface DictionaryDao extends InterfaceHibernateDao<Dictionary> {

    List<Dictionary> getUserDictionaries(User creator);

    List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults);

    Long getDictionaryCount(User creator);

	Long countDictionariesByName(String name);
}
