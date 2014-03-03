package com.dm.service;

import java.util.Collections;
import java.util.List;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.dm.entity.DataDict;

public class DataDictService extends AbstractService<DataDict>{
	
	Cache cache;
	
	public DataDictService() {
		super(DataDict.class);		
		try {
			CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			cache = cacheFactory.createCache(Collections.EMPTY_MAP);
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public List<DataDict> loadDictByType(String code){
		List<DataDict> dataDictList = (List<DataDict>)cache.get(code);
		return dataDictList;
	}
	
}
