package com.dm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.dm.exchange.rest.bean.ColumnOrder;
import com.dm.exchange.rest.bean.DtableColumn;
import com.dm.exchange.rest.bean.DtableOutput;

@Dependent
public class DatatableService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public DtableOutput table(HttpServletRequest request, String tableDef, int draw, int start, int length,
            Map<Integer, ColumnOrder> order, Map<Integer, DtableColumn> columns) {
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
        String orderSql = orderSql(order, columns, dbColumns);
        List<String> columnNames = dbColumns.stream().map((dtableColumn) -> {
            return dtableColumn.getName();
        }).collect(Collectors.toList());
        String sql = "select " + StringUtils.join(columnNames, ",") + " from " + tableName;
        Query query = entityManager.createNativeQuery(sql + " " + orderSql);
        query.setFirstResult(start);
        query.setMaxResults(length);        
        List<Object[]> list = query.getResultList();
        Query filteredCountQuery = entityManager.createNativeQuery("select count(*) from (" + sql + ") as t");
        Long recordsFiltered = (Long) filteredCountQuery.getSingleResult();
        Query countQuery = entityManager.createNativeQuery("select count(" + primaryKey + ") from " + tableName);
        Long recordsTotal = (Long) countQuery.getSingleResult();
        DtableOutput dtable = new DtableOutput();
        dtable.setDraw(draw);
        dtable.setData(list);
        dtable.setRecordsFiltered(recordsFiltered.intValue());
        dtable.setRecordsTotal(recordsTotal.intValue());
        return dtable;
    }

    private String orderSql(Map<Integer, ColumnOrder> order, Map<Integer, DtableColumn> columns,
            List<DtableColumn> dbColumns) {
        String orderSql = "";
        if (!order.isEmpty()) {
            Map<Integer, String> columnMap = new HashMap<>();
            dbColumns.forEach((dtableColumn) -> {
                columnMap.put(dtableColumn.getDt(), dtableColumn.getName());
            });
            List<String> orderBy = new ArrayList<>();
            order.forEach((idx, columnOrder) -> {
                int columnIdx = columnOrder.getColumnIdx();
                DtableColumn dtableColumn = columns.get(columnIdx);
                if (dtableColumn.isOrderable()) {
                    String dir = columnOrder.getDir().equals("asc") ? "asc" : "desc";
                    orderBy.add(columnMap.get(columnIdx) + " " + dir);
                }
            });
            orderSql = "order by " + StringUtils.join(orderBy, ",");
        }
        return orderSql;
    }

}
