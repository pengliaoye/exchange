package com.dm.dp.sf;

public abstract class Operation {

	public double numberA;
	public double numberB;

	public abstract double getResult();

	public double getNumberA() {
		return numberA;
	}

	public void setNumberA(double numberA) {
		this.numberA = numberA;
	}

	public double getNumberB() {
		return numberB;
	}

	public void setNumberB(double numberB) {
		this.numberB = numberB;
	}

}
