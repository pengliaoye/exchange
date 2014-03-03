package com.dm.dp.iterator;

public class IteratorImpl implements Iterator {
	
	private int index;
	private List list;
	
	public IteratorImpl(List list){
		index = 0;
		this.list = list;
	}

	@Override
	public boolean hasNext() {
		return index < list.getSize();
	}

	@Override
	public Object next() {
		Object obj = list.get(index);
		index++;
		return obj;
	}

}
