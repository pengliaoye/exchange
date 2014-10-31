package com.dm.demo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoDaoTest {

  public static void main(String[] args) {
    try {
      MongoClient mongoClient = new MongoClient();
      DB db = mongoClient.getDB("test");
      DBCollection coll = db.getCollection("test");
      DBCursor cursor = coll.find();
      try {
        while (cursor.hasNext()) {
          System.out.println(cursor.next());
        }
      } finally {
        cursor.close();
      }
      mongoClient.close();
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
