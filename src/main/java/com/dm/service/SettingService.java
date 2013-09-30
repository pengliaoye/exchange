package com.dm.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dm.entity.GeneralProperty;

public class SettingService extends AbstractService<GeneralProperty> {

	@PersistenceContext
	private EntityManager entityManager;

	public SettingService() {
		super(GeneralProperty.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
