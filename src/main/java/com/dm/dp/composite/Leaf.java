package com.dm.dp.composite;

public class Leaf extends Component {

  public Leaf(String name) {
    super(name);
  }

  @Override
  public void add(Component component) {

  }

  @Override
  public void remove(Component component) {

  }

  @Override
  public void display(int depth) {
    for (int i = 0; i < depth; i++) {
      System.out.print("-");
    }
    System.out.println(name);
  }

}
