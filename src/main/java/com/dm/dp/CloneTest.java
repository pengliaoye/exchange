package com.dm.dp;

import com.dm.dp.builder.Person;

public class CloneTest {

  /**
   * @param args
   * @throws CloneNotSupportedException
   */
  public static void main(String[] args) throws CloneNotSupportedException {

    Person person = new Person();
    person.setHead("aaa");
    person.setBody("123");
    Person ps = (Person) person.clone();
    person.setHead("bbb");
    System.out.println(person.getHead());
    System.out.println(ps.getHead());
  }

}
