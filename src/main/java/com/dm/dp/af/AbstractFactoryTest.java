package com.dm.dp.af;

public class AbstractFactoryTest {

  /**
   * @param args
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
      IllegalAccessException {
    // reflect + configuration
    String factoryClassName = "com.dm.dp.af.SqlServerFactory";
    Class<?> cls = Class.forName(factoryClassName);
    DBFactory factory = (DBFactory) cls.newInstance();

    // DBFactory factory = new SqlServerFactory();
    IUser user = factory.createUser();
    user.insertUser();

    IDepartment department = factory.createDepartment();
    department.insertDepartment();
  }

}
