package com.dm.dp.builder;

public class PersonDirector {
	
	Builder builder;

	public PersonDirector(Builder builder){
		this.builder = builder;
	}
	
	public void constract(){
		builder.buildHead();
		builder.buildBody();		
	}

}
