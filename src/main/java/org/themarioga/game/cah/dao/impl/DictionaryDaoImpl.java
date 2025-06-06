package org.themarioga.game.cah.dao.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.game.commons.dao.AbstractHibernateDao;
import org.themarioga.game.commons.models.User;
import org.themarioga.game.cah.dao.intf.DictionaryDao;
import org.themarioga.game.cah.models.Dictionary;

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
        return getCurrentSession().createQuery("SELECT t FROM Dictionary t WHERE t.lang=:lang and t.published=true and (t.shared=true or (t.shared=false and t.creator=:creator))", Dictionary.class).setParameter("lang", creator.getLang()).setParameter("creator", creator).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Override
    public Long getDictionaryCount(User creator) {
        return getCurrentSession().createQuery("SELECT count(*) FROM Dictionary t WHERE t.lang=:lang and t.published=true and (t.shared=true or (t.shared=false and t.creator=:creator))", Long.class).setParameter("lang", creator.getLang()).setParameter("creator", creator).getSingleResult();
    }

    @Override
    public Long countDictionariesByName(String name) {
        return getCurrentSession().createQuery("SELECT count(d) FROM Dictionary d WHERE d.name LIKE :name", Long.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public boolean isDictionaryCollaborator(Dictionary dictionary, User user) {
        return getCurrentSession().createQuery("SELECT count(c) FROM DictionaryCollaborator c WHERE c.dictionary=:dictionary and c.user=:user", Long.class).setParameter("dictionary", dictionary).setParameter("user", user).getSingleResultOrNull() > 0;
    }

    @Override
    public boolean isDictionaryActiveCollaborator(Dictionary dictionary, User user) {
        return getCurrentSession().createQuery("SELECT count(c) FROM DictionaryCollaborator c WHERE c.dictionary=:dictionary and c.user=:user and accepted=true and canEdit=true", Long.class).setParameter("dictionary", dictionary).setParameter("user", user).getSingleResultOrNull() > 0;
    }

}

