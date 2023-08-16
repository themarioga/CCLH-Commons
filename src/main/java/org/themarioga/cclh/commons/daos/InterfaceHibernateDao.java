package org.themarioga.cclh.commons.daos;

import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public interface InterfaceHibernateDao<T extends Serializable> {
    // API
    T create(T entity);

    T update(T entity);

    void delete(T entity);

    void deleteById(long entityId);

    T findOne(long id);

    List<T> findAll();

    Long countAll();

    Session getCurrentSession();
}
