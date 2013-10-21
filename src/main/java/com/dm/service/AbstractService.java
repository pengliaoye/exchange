package com.dm.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public abstract class AbstractService<T> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private Class<T> entityClass;

	public AbstractService(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

        @Transactional
	public void create(T entity) {
        	entityManager.persist(entity);
	}

	public void edit(T entity) {
		entityManager.merge(entity);
	}

	public void remove(T entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	public T find(Object id) {
		return entityManager.find(entityClass, id);
	}

	public List<T> getAll() {
		javax.persistence.criteria.CriteriaQuery<T> cq = entityManager
				.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return entityManager.createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery<T> cq = entityManager
				.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		javax.persistence.TypedQuery<T> q = entityManager.createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery<Long> cq = entityManager
				.getCriteriaBuilder().createQuery(Long.class);
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(entityManager.getCriteriaBuilder().count(rt));
		javax.persistence.TypedQuery<Long> q = entityManager.createQuery(cq);
		return (q.getSingleResult()).intValue();
	}
}
