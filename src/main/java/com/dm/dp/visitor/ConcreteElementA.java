package com.dm.dp.visitor;

public class ConcreteElementA implements Visitable {

	@Override
	public void accept(Visitor visitor) {
		visitor.visitConcreteElementA(this);
	}

}
