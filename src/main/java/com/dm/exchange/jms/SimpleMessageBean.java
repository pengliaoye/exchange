package com.dm.exchange.jms;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(mappedName = "jms/myQueue")
public class SimpleMessageBean implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			String msg = message.getBody(String.class);
			System.out.println(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
}
