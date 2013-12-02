package com.dm.dp.interpret;

public class NonTerminalExpression extends AbstractExpression {

	@Override
	public void interpret(Context ctx) {
		System.out.println("non terminal expression");
	}

}
