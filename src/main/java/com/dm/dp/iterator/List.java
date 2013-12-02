package com.dm.dp.iterator;

public interface List {
	
	public Iterator iterator();
	
	Object get(int index);
	
	int getSize();
	
	void add(Object obj);
	
}
