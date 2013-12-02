package com.dm.dp.builder;

public class BuilderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PersonBuilder builder = new PersonBuilder();
		PersonDirector director = new PersonDirector(builder);
		director.constract();
		Person person = builder.getResult();
		System.out.println(person.getHead());
		System.out.println(person.getBody());
	}

}
