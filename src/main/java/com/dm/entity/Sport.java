package com.dm.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tb_sports")
public class Sport {

	@Id
	private String id;

	@NotNull
	@Size(min = 1, max = 200)
	private String name;

}
