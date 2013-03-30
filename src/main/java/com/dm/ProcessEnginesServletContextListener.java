package com.dm;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.activiti.engine.ProcessEngines;

public class ProcessEnginesServletContextListener implements
		ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ProcessEngines.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ProcessEngines.init();
	}

}
