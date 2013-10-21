package com.dm.service;

import javax.enterprise.context.Dependent;

import com.dm.entity.GeneralProperty;

@Dependent
public class SettingService extends AbstractService<GeneralProperty> {

	public SettingService() {
		super(GeneralProperty.class);
	}

}
