package com.dm.dp.fm;


public class UndergraduateFactory implements IFactory {

  @Override
  public LeiFeng createLeiFeng() {
    return new Undergraduate();
  }

}
