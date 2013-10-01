package com.dm.util;

import java.util.Properties;

import org.owasp.esapi.reference.crypto.EncryptedPropertiesUtils;
import org.owasp.esapi.reference.crypto.JavaEncryptor;


public class EncryptPropUtil {

	public static void main(String[] args) throws Exception {
		Properties props = EncryptedPropertiesUtils.loadProperties(null, true);	
		EncryptedPropertiesUtils.addProperty(props, "dataSource.driverClass", "org.postgresql.Driver");
		EncryptedPropertiesUtils.addProperty(props, "dataSource.jdbcUrl", "jdbc:postgresql://localhost/exchange");
		EncryptedPropertiesUtils.addProperty(props, "dataSource.user", "exchange");
		EncryptedPropertiesUtils.addProperty(props, "dataSource.password", "Admin123456");
		EncryptedPropertiesUtils.storeProperties("init.properties", props, "DataSource");
		//JavaEncryptor.main(args);
	}

}
