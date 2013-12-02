package com.dm.dp.cor;

public class ConcreteHandler1 extends Handler {

	@Override
	public void handleRequest(int request) {
		if(request >= 0 && request < 10){
			System.out.println(this.getClass().getName() + " process " + request);
		} else if(successor != null){
			successor.handleRequest(request);
		}
	}

}
