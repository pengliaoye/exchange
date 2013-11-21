package com.dm.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {

	private static EntityManagerFactory emf;

	static {
		emf = Persistence.createEntityManagerFactory("test");
	}

	public static EntityManager getEntityManager() {
		EntityManager em = emf.createEntityManager();
		return em;
	}

	public static void closeEm(EntityManager em) {
		if (em != null) {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

}
