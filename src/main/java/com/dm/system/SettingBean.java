package com.dm.system;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dm.entity.Setting;
import com.dm.service.SettingService;
import java.util.logging.Logger;

@Model
public class SettingBean {
    
        private static final Logger logger = Logger.getLogger(SettingBean.class.getName());

	@Inject
	private SettingService settingService;
	
	public void saveGeneralProp() throws Exception{
            logger.info("test message");
            Setting entity = new Setting();
            entity.setName("url");
            entity.setValue("123");
            settingService.create(entity);
	}
	
}
