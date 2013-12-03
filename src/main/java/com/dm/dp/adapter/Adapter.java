package com.dm.dp.adapter;

public class Adapter implements Target{
	
	private Adaptee adaptee = new Adaptee();;
	
	public Adapter(){
	}

	@Override
	public void request() {
		adaptee.specialRequest();
	}

}
