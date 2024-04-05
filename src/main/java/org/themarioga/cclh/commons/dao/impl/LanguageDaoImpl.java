package org.themarioga.cclh.commons.dao.impl;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.intf.LanguageDao;
import org.themarioga.cclh.commons.models.Lang;

import java.util.List;

@Repository
public class LanguageDaoImpl implements LanguageDao {

    private final EntityManager entityManager;

    @Autowired
    public LanguageDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Lang getLanguage(String id) {
        return getCurrentSession().createQuery("SELECT l FROM Lang l where l.id = :id", Lang.class).setParameter("id", id).getSingleResultOrNull();
    }

    @Override
    public boolean checkLanguageExists(String id) {
        return getCurrentSession().createQuery("SELECT count(l) FROM Lang l where l.id = :id", Long.class).setParameter("id", id).getSingleResultOrNull() > 0;
    }

    @Override
    public List<Lang> getLanguages() {
        return getCurrentSession().createQuery("SELECT l FROM Lang l", Lang.class).getResultList();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }
}
