package com.dm.dp.iterator;

public class IteratorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ListImpl();
		list.add(123);
		list.add(456);
		list.add(789);
		
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		
		for(int i = 0; i < list.getSize(); i++){
			System.out.println(list.get(i));
		}
	}

}
