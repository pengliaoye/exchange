package com.dm.dp.decorator;

public class ConcreteComponent implements Component {

  @Override
  public void operation() {
    System.out.println("special object operation");
  }

}
