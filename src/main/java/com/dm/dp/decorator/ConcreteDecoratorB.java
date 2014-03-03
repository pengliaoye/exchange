package com.dm.dp.decorator;

public class ConcreteDecoratorB extends Decorator{

	@Override
	public void operation() {
		super.operation();
		addedBehavior();
		System.out.println("object B operation");
	}
	
	public void addedBehavior(){
		
	}

}
