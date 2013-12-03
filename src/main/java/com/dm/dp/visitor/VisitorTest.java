package com.dm.dp.visitor;

public class VisitorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ObjectStructure oc = new ObjectStructure();
		oc.attach(new ConcreteElementA());
		oc.attach(new ConcreteElementB());
		
		ConcreteVisitor1 visitor1 = new ConcreteVisitor1();
		ConcreteVisitor2 visitor2 = new ConcreteVisitor2();
		oc.accept(visitor1);
		oc.accept(visitor2);
	}

}
