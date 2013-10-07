package com.dm.system;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import com.dm.entity.GeneralProperty;
import com.dm.service.SettingService;
import javax.annotation.Resource;
import javax.transaction.UserTransaction;

@Model
public class SettingBean {
    
        @Resource
        UserTransaction utx;

	@Inject
	private SettingService settingService;
	
	public void saveGeneralProp() throws Exception{
            try{
                utx.begin();
		GeneralProperty entity = new GeneralProperty();
		entity.setName("url");
		entity.setValue("123");
		settingService.create(entity);
                utx.commit();
            } catch (Exception e){
                try {
                    utx.rollback();
                } catch (Exception re) {
                   re.printStackTrace();
                }
                e.printStackTrace();                
            }
	}
	
}
