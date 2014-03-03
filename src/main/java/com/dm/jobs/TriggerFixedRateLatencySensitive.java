/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.jobs;

import java.util.Date;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.Trigger;

/**
 *
 * @author cmp
 */
public class TriggerFixedRateLatencySensitive implements Trigger {

    private Date startTime;
    private long delta;
    private long latencyAllowance;

    public TriggerFixedRateLatencySensitive(Date startTime, long delta, long latencyAllowance) {
        this.startTime = startTime;
        this.delta = delta;
        this.latencyAllowance = latencyAllowance;
    }

    @Override
    public Date getNextRunTime(LastExecution lastExecutionInfo,
            Date taskScheduledTime) {
        if (lastExecutionInfo == null) {
            return startTime;
        }        
        return new Date(lastExecutionInfo.getScheduledStart().getTime() + delta);
    }

    @Override
    public boolean skipRun(LastExecution lastExecutionInfo, Date scheduledRunTime) {
        return System.currentTimeMillis() - scheduledRunTime.getTime() > latencyAllowance;        
    }
}
