package com.dm.dp.proxy;

import org.junit.Test;

public class DynamicProxyTest {
	@Test
	public void testDynamicProxy() {
		Foo foo = (Foo) DynamicProxy.newInstance(new FooImpl());
		System.out.println(foo.bar(null));
	}
}
