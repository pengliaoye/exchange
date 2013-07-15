package com.dm;

import com.dm.jobs.TriggerFixedRateLatencySensitive;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.concurrent.Trigger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.activiti.engine.ProcessEngines;
import org.apache.commons.dbutils.QueryLoader;

public class AppServletContextListener implements
        ServletContextListener {

    private final static Logger logger = Logger.getLogger(AppServletContextListener.class.getCanonicalName());
    @Resource
    private ManagedScheduledExecutorService executor;

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
        Trigger trigger = new TriggerFixedRateLatencySensitive(new Date(), 30 * 1000, 5 * 10);
        //executor.submit(new JobTask("1"), trigger);
        executor.scheduleAtFixedRate(new JobTask("1"), 0l, 5l, TimeUnit.SECONDS);
    }

    static class JobTask implements Runnable {

        private final String jobID;
        private final int JOB_EXECUTION_TIME = 10000;

        public JobTask(String id) {
            this.jobID = id;
        }

        @Override
        public void run() {
            try {
                logger.log(Level.INFO, "Task started {0}", jobID);
                Thread.sleep(JOB_EXECUTION_TIME); // 5 seconds per job
                logger.log(Level.INFO, "Task finished {0}", jobID);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
}
