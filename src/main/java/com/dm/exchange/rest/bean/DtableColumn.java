package com.dm.exchange.rest.bean;

import java.util.function.BiFunction;

public class DtableColumn {

	private String name;
	private int dt;
	private BiFunction formatter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}

}
