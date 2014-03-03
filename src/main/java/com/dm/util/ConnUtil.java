package com.dm.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.owasp.esapi.reference.crypto.EncryptedPropertiesUtils;
import org.springframework.util.ResourceUtils;

public class ConnUtil {
    
	public static Connection getConn() throws IOException, ClassNotFoundException, SQLException{
    	File file = ResourceUtils.getFile("classpath:init.properties");
    	Properties props = EncryptedPropertiesUtils.loadProperties(file.getPath(), true);    	
    	String url = props.getProperty("dataSource.jdbcUrl");
    	String username = props.getProperty("dataSource.user");
    	String password = props.getProperty("dataSource.password");
    	Connection conn = DriverManager.getConnection(url, username, password);
    	return conn;
    }
	
}
