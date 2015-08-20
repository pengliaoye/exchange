package com.dm.exchange.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.dm.entity.Sport;

@Named
@Stateless
@Path("sports")
public class SportFacadeREST extends AbstractFacade<Sport> {

	@PersistenceContext
	protected EntityManager em;

	public SportFacadeREST() {
		super(Sport.class);
	}

	@GET
	@Override
	@Produces({ "application/xml", "application/json" })
	public List<Sport> getAll() {
		return super.getAll();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
