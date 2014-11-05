/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.dm.demo;

import com.google.common.io.Files;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmp
 */
public class CuratorDemo implements AutoCloseable {

  private static final Logger logger = Logger.getLogger(CuratorDemo.class.getName());

  public static final String CONFIGS_ZNODE = "/configs";

  private CuratorFramework client;

  public CuratorDemo() {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    client =
        CuratorFrameworkFactory.builder().connectString("localhost:2181").retryPolicy(retryPolicy)
            .namespace("cmp").build();
    client.start();
  }

  public static void main(String[] args) {
    try (CuratorDemo demo = new CuratorDemo()) {
      File dir =
          new File("C:/Users/cmp/Downloads/solr-4.10.1/solr-4.10.1/node1/solr/collection1/conf");
      demo.uploadConfigDir(dir, "myconf");
    } catch (Exception ex) {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  public void uploadConfigDir(File dir, String configName) throws Exception {
    this.uploadToZk(client, dir, CuratorDemo.CONFIGS_ZNODE + "/" + configName);
  }

  public void uploadToZk(CuratorFramework client, File dir, String zkPath) throws Exception {
    File[] files = dir.listFiles();
    if (files == null) {
      throw new IllegalArgumentException("Illegal directory: " + dir);
    }
    for (File file : files) {
      if (!file.getName().startsWith(".")) {
        if (!file.isDirectory()) {
          client.create().creatingParentsIfNeeded()
              .forPath(zkPath + "/" + file.getName(), Files.toByteArray(file));
        } else {
          uploadToZk(client, file, zkPath + "/" + file.getName());
        }
      }
    }
  }

  public void close() {
    client.close();
  }
}
