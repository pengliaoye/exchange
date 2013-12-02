package com.dm.dp.memento;

public class Originator {
	
	private String state;
	
	public Memento createMemento(){
		return new Memento(state);
	}
	
	public void setMemento(Memento memento){
		this.state = memento.getState();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public void showState(){
		System.out.println(state);
	}

}
