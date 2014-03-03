package com.dm.dp.cor;

public abstract class Handler {

	protected Handler successor;

	public abstract void handleRequest(int request);

	public void setSuccessor(Handler successor) {
		this.successor = successor;
	}

}
