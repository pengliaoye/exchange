package com.dm.dp;

public class SingletonTest {

  public static void main(String[] args) {
    Singleton instance1 = Singleton.getInstance();
    Singleton instance2 = Singleton.getInstance();
    System.out.println(instance1);
    System.out.println(instance2);
  }

}
