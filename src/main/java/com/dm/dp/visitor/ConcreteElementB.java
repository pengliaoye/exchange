package com.dm.dp.visitor;

public class ConcreteElementB implements Visitable {

	@Override
	public void accept(Visitor visitor) {
		visitor.visitConcreteElementB(this);
	}

}
