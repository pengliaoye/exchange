package com.dm.dp.mediator;

public abstract class Colleague {
	
	protected Mediator mediator;
	
	public Colleague(Mediator mediator){
		this.mediator = mediator;
	}
	
}
