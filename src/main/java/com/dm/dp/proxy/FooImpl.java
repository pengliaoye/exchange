package com.dm.dp.proxy;

public class FooImpl implements Foo{
	@Override
	public Object bar(Object obj) {
		return "foo";
	}
}
