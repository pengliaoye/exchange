package com.dm.dp.sf;


public class FactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Operation oper = OperationFactory.createOperate("+");
		oper.numberA = 1;
		oper.numberB = 2;
		double result = oper.getResult();
		System.out.println(result);
	}

}
