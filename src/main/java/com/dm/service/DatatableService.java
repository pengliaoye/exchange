package com.dm.service;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.servlet.http.HttpServletRequest;

import com.dm.exchange.rest.bean.DtableColumn;
import com.dm.exchange.rest.bean.DtableOutput;

@Dependent
public class DatatableService {

	public DtableOutput table(HttpServletRequest request, String tableDef, int start, int length) {
		String tableName = null;
		String primaryKey = null;
		List<DtableColumn> columns = null;
		String limitSql = limitSql(start, length);
		DtableOutput dtable = new DtableOutput();
		return dtable;
	}

	private String limitSql(int start, int length) {
		String limitSql = "limit " + start + ", " + length;
		return limitSql;
	}

}
