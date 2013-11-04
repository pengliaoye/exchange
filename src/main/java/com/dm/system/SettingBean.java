package com.dm.system;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dm.entity.Setting;
import com.dm.service.SettingService;

@Model
public class SettingBean {

	@Inject
	private SettingService settingService;
	
	public void saveGeneralProp() throws Exception{
            Setting entity = new Setting();
            entity.setName("url");
            entity.setValue("123");
            settingService.create(entity);
	}
	
}
