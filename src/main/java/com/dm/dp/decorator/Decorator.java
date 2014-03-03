package com.dm.dp.decorator;

public class Decorator implements Component{
	
	private Component component;

	public void setComponent(Component component) {
		this.component = component;
	}
	
	@Override
	public void operation() {
		if(component != null){
			component.operation();
		}
	}

}
