package com.dm.dp.fm;


public class FactoryMethodTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IFactory factory = new UndergraduateFactory();
		LeiFeng student = factory.createLeiFeng();
		student.doSomething();
	}

}
