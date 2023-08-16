package org.themarioga.cclh.daos.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.CardDao;
import org.themarioga.cclh.daos.intf.ConfigurationDao;
import org.themarioga.cclh.models.Card;

@Repository
public class ConfigurationDaoImpl implements ConfigurationDao {

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public String getConfiguration(String key) {
        return sessionFactory.getCurrentSession().createNativeQuery("SELECT conf_value FROM t_configuration where conf_key = ?", String.class).setParameter(1, key).getSingleResult();
    }

}
