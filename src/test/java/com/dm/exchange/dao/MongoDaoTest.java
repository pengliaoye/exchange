package com.dm.exchange.dao;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoDaoTest {

	//@Test
	public void testDb() {
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
