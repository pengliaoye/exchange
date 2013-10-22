package com.dm.exchange.jms;

import javax.annotation.Resource;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Model
public class JmsSendBean {
	
	@Inject
	JMSContext context; 
	
	@Resource(mappedName = "jms/myQueue")
	Queue pointsQueue;

	public void sendMessage(){
		context.createProducer().send(pointsQueue, "hello jms");
	}
	
}
