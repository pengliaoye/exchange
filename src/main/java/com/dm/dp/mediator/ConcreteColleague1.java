package com.dm.dp.mediator;

public class ConcreteColleague1 extends Colleague {

	public ConcreteColleague1(Mediator mediator) {
		super(mediator);
	}
	
	public void send(String message){
		mediator.send(message, this);
	}
	
	public void notify(String message){
		System.out.println("ConreteColleague1 " + message);
	}

}
