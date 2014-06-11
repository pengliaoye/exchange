package com.dm.dp.interpret;

import java.util.ArrayList;
import java.util.List;

public class InterpretTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Context ctx = new Context();
		List<AbstractExpression> list = new ArrayList<AbstractExpression>();
		list.add(new TerminalExpression());
		list.add(new NonTerminalExpression());
                list.stream().forEach((expression) -> {
                expression.interpret(ctx);
            });
	}

}
