package com.dm.dp.visitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectStructure {

	private List<Visitable> elements = new ArrayList<Visitable>();
	
	public void attach(Visitable visitable){
		elements.add(visitable);
	}
	
	public void detach(Visitable visitable){
		elements.remove(visitable);
	}
	
	public void accept(Visitor visitor){
            elements.stream().forEach((visitable) -> {
                visitable.accept(visitor);
            });
	}
	
}
