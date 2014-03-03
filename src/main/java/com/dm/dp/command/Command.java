package com.dm.dp.command;

public abstract class Command {
	
	public Receiver receiver;
	
	public Command(Receiver receiver){
		this.receiver = receiver;
	}
	
	public abstract void execute();
}
