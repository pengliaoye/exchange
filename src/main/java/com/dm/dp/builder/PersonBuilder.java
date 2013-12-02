package com.dm.dp.builder;

public class PersonBuilder implements Builder {
	
	Person person;
	
	public PersonBuilder(){
		person = new Person();
	}

	@Override
	public void buildHead() {
		person.setHead("head");
	}

	@Override
	public void buildBody() {
		person.setBody("body");
	}

	public Person getResult(){
		return person;
	}

}
