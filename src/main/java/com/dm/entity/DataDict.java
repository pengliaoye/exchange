package com.dm.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_dict")
public class DataDict {

	@Id
	private String id;
	private String code;
	private String name;
	private String description;

	@ManyToOne
	@JoinColumn(name = "type", referencedColumnName = "id")
	private DataDictType dictType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataDictType getDictType() {
		return dictType;
	}

	public void setDictType(DataDictType dictType) {
		this.dictType = dictType;
	}

}
