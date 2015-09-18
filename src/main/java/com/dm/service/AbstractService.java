package com.dm.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public abstract class AbstractService<T> {

	@PersistenceContext(unitName = "default")
	protected EntityManager entityManager;

	private Class<T> entityClass;

	public AbstractService(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

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
		CriteriaQuery<T> cq = entityManager.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return entityManager.createQuery(cq).getResultList();
	}

	public List<T> findRange(int[] range) {
		CriteriaQuery<T> cq = entityManager.getCriteriaBuilder().createQuery(entityClass);
		cq.select(cq.from(entityClass));
		TypedQuery<T> q = entityManager.createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);
		return q.getResultList();
	}

	public long count() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(builder.count(rt));
		TypedQuery<Long> q = entityManager.createQuery(cq);
		return q.getSingleResult();
	}

	public long countWhere(Root<T> rt, Expression<Boolean> restriction) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = builder.createQuery(Long.class);
		// javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(builder.count(rt)).where(restriction);
		TypedQuery<Long> q = entityManager.createQuery(cq);
		return q.getSingleResult();
	}

}
