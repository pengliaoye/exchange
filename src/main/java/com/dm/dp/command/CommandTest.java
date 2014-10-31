package com.dm.dp.command;

public class CommandTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Invoker invoker = new Invoker();
    Receiver receiver = new Receiver();
    Command command = new ConcreteCommand(receiver);
    invoker.setCommand(command);
    invoker.execute();
  }

}
