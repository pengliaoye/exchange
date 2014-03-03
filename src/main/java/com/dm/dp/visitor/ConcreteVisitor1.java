package com.dm.dp.visitor;

public class ConcreteVisitor1 implements Visitor {

	@Override
	public void visitConcreteElementA(ConcreteElementA concreteElementA) {
		System.out.println("visitor1 visit element a");
	}

	@Override
	public void visitConcreteElementB(ConcreteElementB concreteElementB) {
		System.out.println("visitor1 visit element b");
	}

}
