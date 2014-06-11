package com.dm.dp.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

	private List<Observer> observers = new ArrayList<>();
	
	public void attach(Observer observer){
		observers.add(observer);		
	}
	
	public void detach(Observer observer){
		observers.remove(observer);
	}
	
	public void notifyObservers(){
            observers.stream().forEach((observer) -> {
                observer.update();
            });
	}
	
}
