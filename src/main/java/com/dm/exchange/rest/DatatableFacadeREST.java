package com.dm.exchange.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.dm.exchange.rest.bean.ColumnOrder;
import com.dm.exchange.rest.bean.DtableColumn;
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

	private static final Pattern orderPattern = Pattern.compile("order\\[(\\d)\\]\\[(dir|column)\\]");
	private static final Pattern columnsPattern = Pattern
			.compile("columns\\[(\\d)\\]\\[(data|name|searchable|orderable)\\]");

	@GET
	@Produces("application/json")
	public DtableOutput getAll(@QueryParam("draw") int draw, @QueryParam("start") int start, @QueryParam("length") int length) {
		Map<Integer, ColumnOrder> order = new HashMap<>();
		Map<Integer, DtableColumn> columns = new HashMap<>();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();
			if (name.startsWith("order")) {
				String val = request.getParameter(name);
				Matcher matcher = orderPattern.matcher(name);
				while (matcher.find()) {
					Integer idx = Integer.valueOf(matcher.group(1));
					String colLabel = matcher.group(2);
					ColumnOrder colOrder = order.get(idx);
					if (colOrder == null) {
						colOrder = new ColumnOrder();
						order.put(idx, colOrder);
					}
					if (colLabel.equals("column")) {
						colOrder.setColumnIdx(Integer.valueOf(val));
					}
					if (colLabel.equals("dir")) {
						colOrder.setDir(val);
					}
				}
			} else if (name.startsWith("columns")) {
				String val = request.getParameter(name);
				Matcher matcher = columnsPattern.matcher(name);
				while (matcher.find()) {
					Integer idx = Integer.valueOf(matcher.group(1));
					String colLabel = matcher.group(2);
					DtableColumn column = columns.get(idx);
					if (column == null) {
						column = new DtableColumn();
						columns.put(idx, column);
					}
					if (colLabel.equals("name")) {
						column.setColName(val);
					}
					if (colLabel.equals("orderable")) {
						column.setOrderable(Boolean.valueOf(val));
					}
					if (colLabel.equals("searchable")) {
						column.setSearchable(Boolean.valueOf(val));
					}
					if (colLabel.equals("data")) {
						column.setData(val);
					}
				}
			}
		}
		DtableOutput output = dtableService.table(request, null, draw, start, length, order, columns);
		return output;
	}

}
