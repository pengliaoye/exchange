package com.dm.dp.visitor;

public class ConcreteVisitor2 implements Visitor {

	@Override
	public void visitConcreteElementA(ConcreteElementA concreteElementA) {
		System.out.println("visitor2 visit element a");
	}

	@Override
	public void visitConcreteElementB(ConcreteElementB concreteElementB) {
		System.out.println("visitor2 visit element b");
	}

}
