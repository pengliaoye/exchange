package com.dm.dp.memento;

public class MementoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Originator originator = new Originator();
		originator.setState("state1");
		
		Caretaker caretaker = new Caretaker();
		caretaker.setMemento(originator.createMemento());
		
		originator.setState("state2");
		originator.showState();
		
		originator.setMemento(caretaker.getMemento());
		originator.showState();
	}

}
