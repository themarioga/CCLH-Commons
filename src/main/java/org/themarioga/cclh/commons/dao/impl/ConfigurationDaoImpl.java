package org.themarioga.cclh.commons.dao.impl;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.dao.intf.ConfigurationDao;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

    @Autowired
    protected EntityManager entityManager;

    @Override
    public String getConfiguration(String key) {
        return getCurrentSession().createNativeQuery("SELECT conf_value FROM t_configuration where conf_key = ?", String.class).setParameter(1, key).getSingleResultOrNull();
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

}
