package org.themarioga.cclh.commons.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.AbstractHibernateDao;
import org.themarioga.cclh.commons.dao.intf.DictionaryDao;
import org.themarioga.cclh.commons.models.Dictionary;
import org.themarioga.cclh.commons.models.User;

import java.util.List;

@Repository
public class DictionaryDaoImpl extends AbstractHibernateDao<Dictionary> implements DictionaryDao {

    public DictionaryDaoImpl() {
        setClazz(Dictionary.class);
    }

    @Override
    public List<Dictionary> getDictionariesByCreator(User user) {
        return getCurrentSession().createQuery("SELECT t FROM Dictionary t WHERE t.creator=:user", Dictionary.class).setParameter("user", user).getResultList();
    }

    @Override
    public List<Dictionary> getDictionariesByCreatorOrCollaborator(User user) {
        return getCurrentSession().createQuery("SELECT t FROM Dictionary t WHERE t.creator=:user or t IN (SELECT c.dictionary FROM DictionaryCollaborator c WHERE c.user=:user)", Dictionary.class).setParameter("user", user).getResultList();
    }

    @Override
    public List<Dictionary> getDictionariesPaginated(User creator, int firstResult, int maxResults) {
        return getCurrentSession().createQuery("SELECT t FROM Dictionary t WHERE t.published=true and (t.shared=true or (t.shared=false and t.creator=:creator))", Dictionary.class).setParameter("creator", creator).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
    public Long getDictionaryCount(User creator) {
        return getCurrentSession().createQuery("SELECT count(*) FROM Dictionary t WHERE t.published=true and (t.shared=true or (t.shared=false and t.creator=:creator))", Long.class).setParameter("creator", creator).getSingleResult();
    }

    @Override
    public Long countDictionariesByName(String name) {
        return getCurrentSession().createQuery("SELECT count(d) FROM Dictionary d WHERE d.name LIKE :name", Long.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public boolean isDictionaryCollaborator(Dictionary dictionary, User user) {
        return getCurrentSession().createQuery("SELECT count(c) FROM DictionaryCollaborator c WHERE c.dictionary=:dictionary and c.user=:user and accepted=true and canEdit=true", Long.class).setParameter("dictionary", dictionary).setParameter("user", user).getSingleResultOrNull() > 0;
    }

}

