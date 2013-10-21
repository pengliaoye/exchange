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

	protected  EntityManager getEntityManager(){
		return this.entityManager;
	}

        @Transactional
	public void create(T entity) {
		getEntityManager().persist(entity);
	}

	public void edit(T entity) {
		getEntityManager().merge(entity);
	}

	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	public List<T> getAll() {
		javax.persistence.criteria.CriteriaQuery<T> cq = getEntityManager()
				.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery<T> cq = getEntityManager()
				.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		javax.persistence.TypedQuery<T> q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public int count() {
		javax.persistence.criteria.CriteriaQuery<Long> cq = getEntityManager()
				.getCriteriaBuilder().createQuery(Long.class);
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.TypedQuery<Long> q = getEntityManager().createQuery(cq);
		return (q.getSingleResult()).intValue();
	}
}
