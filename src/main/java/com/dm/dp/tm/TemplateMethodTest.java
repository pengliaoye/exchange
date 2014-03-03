package com.dm.dp.tm;

public class TemplateMethodTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractClass as = null;
		as = new ConcreateClassA();
		as.tempateMethod();
		
		as = new ConcreateClassB();
		as.tempateMethod();		
	}

}
