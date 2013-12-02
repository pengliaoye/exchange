package com.dm.dp.observer;

public class ConcreteObserver implements Observer {
	
	private String name;
	private ConcreteSubject subject;
	private String observerState;
	
	public ConcreteObserver(ConcreteSubject subject, String name){
		this.subject = subject;
		this.name = name;
	}

	@Override
	public void update() {
		observerState = subject.getSubjectState();
		System.out.println(name + " new state " + observerState);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConcreteSubject getSubject() {
		return subject;
	}

	public void setSubject(ConcreteSubject subject) {
		this.subject = subject;
	}

	public String getObserverState() {
		return observerState;
	}

	public void setObserverState(String observerState) {
		this.observerState = observerState;
	}

}
