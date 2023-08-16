package org.themarioga.cclh.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.daos.AbstractHibernateDao;
import org.themarioga.cclh.daos.intf.DictionaryDao;
import org.themarioga.cclh.models.Dictionary;
import org.themarioga.cclh.models.Room;

@Repository
public class DictionaryDaoImpl extends AbstractHibernateDao<Dictionary> implements DictionaryDao {

    public DictionaryDaoImpl() {
        setClazz(Dictionary.class);
    }

}
