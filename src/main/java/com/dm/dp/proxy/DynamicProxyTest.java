package com.dm.dp.proxy;

public class DynamicProxyTest {
	
	public static void main(String args) {
		Foo foo = (Foo) DynamicProxy.newInstance(new FooImpl());
		System.out.println(foo.bar(null));
	}
}
