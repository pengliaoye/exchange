package com.dm;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.activiti.engine.ProcessEngines;
import org.apache.commons.dbutils.QueryLoader;

public class AppServletContextListener implements
		ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ProcessEngines.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		try {
			QueryLoader.instance().load("/Queries.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		ProcessEngines.init();
	}

}
