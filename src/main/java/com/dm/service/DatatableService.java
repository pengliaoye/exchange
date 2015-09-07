package com.dm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.servlet.http.HttpServletRequest;

import com.dm.entity.Sport;
import com.dm.exchange.rest.AbstractFacade;
import com.dm.exchange.rest.bean.ColumnOrder;
import com.dm.exchange.rest.bean.DtableColumn;
import com.dm.exchange.rest.bean.DtableOutput;

@Dependent
public class DatatableService extends AbstractFacade<Sport> {

	public DatatableService() {
		super(Sport.class);
	}

	@PersistenceContext(unitName = "default")
	private EntityManager entityManager;

	public DtableOutput table(HttpServletRequest request, String tableDef, int draw, int start, int length,
			String search, Map<Integer, ColumnOrder> order, Map<Integer, DtableColumn> columns) {
		String tableName = "tb_sports";
		String primaryKey = "id";
		List<DtableColumn> dbColumns = new ArrayList<>();
		DtableColumn col1 = new DtableColumn();
		col1.setName("id");
		col1.setDt(0);
		DtableColumn col2 = new DtableColumn();
		col2.setName("name");
		col2.setDt(1);
		dbColumns.add(col1);
		dbColumns.add(col2);
		Map<Integer, String> columnMap = new HashMap<>();
		dbColumns.forEach((dtableColumn) -> {
			columnMap.put(dtableColumn.getDt(), dtableColumn.getName());
		});

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
		Root<Sport> sportRoot = criteriaQuery.from(Sport.class);
		List<Selection<?>> columnNames = dbColumns.stream().map((dtableColumn) -> {
			return sportRoot.get(dtableColumn.getColName());
		}).collect(Collectors.toList());
		criteriaQuery.multiselect(columnNames);
		// criteriaQuery.where(builder.equal(sportRoot.get("name"), "Ram"));
		if (!order.isEmpty()) {
			List<Order> orders = order.values().stream().map((columnOrder) -> {
				Expression<?> orderExp = sportRoot.get(columnMap.get(columnOrder.getColumnIdx()));
				if (columnOrder.equals("asc")) {
					return builder.asc(orderExp);
				} else {
					return builder.desc(orderExp);
				}
			}).collect(Collectors.toList());
			criteriaQuery.orderBy(orders);
		}
		Predicate[] globalSearch = null;
		if (search != null) {
			globalSearch = columns.keySet().stream().map((columnIdx) -> {
				return builder.like(sportRoot.get(columnMap.get(columnIdx)), "%" + search + "%");
			}).toArray((size -> new Predicate[size]));
			criteriaQuery.where(globalSearch);
		}
		TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery);
		query.setFirstResult(start);
		query.setMaxResults(length);

		List<Object[]> dataArray = query.getResultList();
		long recordsTotal = this.count();
		long recordsFiltered;
		if (search != null) {
			recordsFiltered = this.countWhere(globalSearch);
		} else {
			recordsFiltered = recordsTotal;
		}

		DtableOutput dtable = new DtableOutput();
		dtable.setDraw(draw);
		dtable.setData(dataArray);
		dtable.setRecordsFiltered(recordsFiltered);
		dtable.setRecordsTotal(recordsTotal);
		return dtable;
	}

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}
