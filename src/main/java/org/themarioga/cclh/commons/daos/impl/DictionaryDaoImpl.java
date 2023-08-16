package org.themarioga.cclh.commons.daos.impl;

import org.springframework.stereotype.Repository;
import org.themarioga.cclh.commons.daos.AbstractHibernateDao;
import org.themarioga.cclh.commons.daos.intf.DictionaryDao;
import org.themarioga.cclh.commons.models.Dictionary;

@Repository
public class DictionaryDaoImpl extends AbstractHibernateDao<Dictionary> implements DictionaryDao {

    public DictionaryDaoImpl() {
        setClazz(Dictionary.class);
    }

}
