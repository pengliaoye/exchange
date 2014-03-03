package com.dm.dp.cor;

public class ConcreteHandler3 extends Handler {

	@Override
	public void handleRequest(int request) {
		if(request >= 20 && request < 30){
			System.out.println(this.getClass().getName() + " process " + request);
		} else if(successor != null){
			successor.handleRequest(request);
		}
	}

}
