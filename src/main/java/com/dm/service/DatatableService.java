package com.dm.service;

import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.dm.exchange.rest.bean.DtableColumn;
import com.dm.exchange.rest.bean.DtableOutput;

@Named
public class DatatableService {

	public DtableOutput table(HttpServletRequest request, String tableDef) {
		String tableName = null;
		String primaryKey = null;
		List<DtableColumn> columns = null;
		String limitSql = limitSql(request, columns);
		DtableOutput dtable = new DtableOutput();
		return dtable;
	}

	private String limitSql(HttpServletRequest request, List<DtableColumn> columns) {
		String limitSql = "";

		String start = request.getParameter("start");
		String length = request.getParameter("length");

		return limitSql;
	}

}
