package com.dm.dp.flyweight;

public class ConcreteFlyweight implements Flyweight {

  @Override
  public void operation(int extrinsicstate) {
    System.out.println("Flyweight : " + extrinsicstate);
  }

}
