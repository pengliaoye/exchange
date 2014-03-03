package com.dm.system;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.dm.service.HttpService;

public class HttpServiceTest {
	
	@Test
	public void testHttpConn(){
		String url = "http://www.ems.com.cn/partner/api/public/p/area/cn/province/list";
		HttpService bean = new HttpService();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("version", "international_eub_us_1.1");
		headers.put("authenticate", "emseub_15bbd18f29bd3696a2aad23bcb857829");
		bean.getHttpResp(url, headers);
	}

}
