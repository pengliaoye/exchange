/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.dm.demo;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmp
 */
public class CuratorDemo {

  private static final Logger logger = Logger.getLogger(CuratorDemo.class.getName());

  public static void main(String[] args) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    try (CuratorFramework client =
        CuratorFrameworkFactory.builder().connectString("localhost:2181").namespace("cmp").build()) {
      client.start();

      client.create().forPath("/configs/core.conf");
      client.getChildren().usingWatcher(new CuratorWatcher() {

        @Override
        public void process(WatchedEvent event) throws Exception {
          String data = client.getData().forPath("/configs/core.conf").toString();
          System.out.print(data);
        }

      }).forPath("/configs/core.conf");
    } catch (Exception ex) {
      logger.log(Level.SEVERE, null, ex);
    }
  }
}
