package com.dm.exchange.rest;

import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.dm.exchange.rest.bean.DtableOutput;
import com.dm.service.DatatableService;

@Named
@Stateless
@Path("dtable")
public class DatatableFacadeREST {

	@Context
	private HttpServletRequest request;

	@Inject
	private DatatableService dtableService;

	@GET
	@Produces("application/json")
	public DtableOutput getAll(@QueryParam("start") int start, @QueryParam("length") int length,
			@QueryParam("order") Map<String, Integer>[] order) {
		DtableOutput output = dtableService.table(request, null, start, length);
		return output;
	}

}
