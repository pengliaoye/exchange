package com.dm.dp.decorator;

public class DecoratorTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Component comp = new ConcreteComponent();
    Decorator d1 = new ConcreteDecoratorA();
    Decorator d2 = new ConcreteDecoratorB();

    d1.setComponent(comp);
    d2.setComponent(d1);

    d2.operation();
  }

}
