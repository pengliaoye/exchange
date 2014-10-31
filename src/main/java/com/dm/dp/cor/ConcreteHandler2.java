package com.dm.dp.cor;

public class ConcreteHandler2 extends Handler {

  @Override
  public void handleRequest(int request) {
    if (request >= 10 && request < 20) {
      System.out.println(this.getClass().getName() + " process " + request);
    } else if (successor != null) {
      successor.handleRequest(request);
    }
  }

}
