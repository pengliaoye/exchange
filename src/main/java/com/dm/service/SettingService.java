package com.dm.service;

import javax.enterprise.context.Dependent;

import com.dm.entity.Setting;

@Dependent
public class SettingService extends AbstractService<Setting> {

	public SettingService() {
		super(Setting.class);
	}		

}
