package com.dm.dp.sf;

public class OperationSub extends Operation {

	@Override
	public double getResult() {
		double result = 0;
		result = numberA - numberB; 
		return result;
	}

}
