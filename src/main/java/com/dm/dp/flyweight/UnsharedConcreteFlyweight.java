package com.dm.dp.flyweight;

public class UnsharedConcreteFlyweight implements Flyweight {

	@Override
	public void operation(int extrinsicstate) {
		System.out.println("Unshared Flyweight : " + extrinsicstate);
	}

}
