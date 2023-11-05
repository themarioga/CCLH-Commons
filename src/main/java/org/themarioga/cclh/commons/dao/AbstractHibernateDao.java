package org.themarioga.cclh.commons.dao;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractHibernateDao<T extends Serializable> implements InterfaceHibernateDao<T> {

    private Class<T> clazz;

    @Autowired
    protected EntityManager entityManager;

    public final void setClazz(final Class<T> clazzToSet) {
        clazz = Objects.requireNonNull(clazzToSet);
    }

    // API
    @Override
    public T create(final T entity) {
        Assert.notNull(entity, "No puede ser null");
        getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public T update(final T entity) {
        Assert.notNull(entity, "No puede ser null");
        return getCurrentSession().merge(entity);
    }

    @Override
    public void delete(final T entity) {
        Assert.notNull(entity, "No puede ser null");
        getCurrentSession().remove(entity);
    }

    @Override
    public void deleteById(final long entityId) {
        Assert.notNull(entityId, "No puede ser null");
        final T entity = findOne(entityId);
        delete(entity);
    }

    @Override
    public T findOne(final long id) {
        return getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
    }

    @Override
    public Long countAll() {
        return getCurrentSession().createQuery("from " + clazz.getName(), clazz).stream().count();
    }

    @Override
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

}