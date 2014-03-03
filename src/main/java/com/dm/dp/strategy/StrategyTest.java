package com.dm.dp.strategy;

public class StrategyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Strategy strategy = new ConcreteStrategyA();
		Context context = new Context(strategy);
		context.contextInterface();
		
		context = new Context(new ConcreteStrategyB());
		context.contextInterface();
		
		context = new Context(new ConcreteStrategyC());
		context.contextInterface();
	}

}
