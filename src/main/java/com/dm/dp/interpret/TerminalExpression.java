package com.dm.dp.interpret;

public class TerminalExpression extends AbstractExpression {

	@Override
	public void interpret(Context ctx) {
		System.out.println("terminal expression");
	}

}
