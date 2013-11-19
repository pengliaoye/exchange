package com.dm.service;

import javax.enterprise.context.Dependent;

import com.dm.entity.Food;

@Dependent
public class FoodService extends AbstractService<Food>{

	public FoodService() {
		super(Food.class);
	}

}
