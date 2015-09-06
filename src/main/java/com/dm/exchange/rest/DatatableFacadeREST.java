package com.dm.exchange.rest;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.dm.exchange.rest.bean.DtableOutput;

@Named
@Stateless
@Path("dtable")
public class DatatableFacadeREST {

	@GET
	@Produces("application/json")
	public DtableOutput getAll() {
		DtableOutput output = new DtableOutput();
		return output;
	}

}
