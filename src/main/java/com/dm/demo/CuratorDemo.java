/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dm.demo;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 *
 * @author cmp
 */
public class CuratorDemo {
    
    private static final Logger logger = Logger.getLogger(CuratorDemo.class.getName());

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        try (CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);) {
            client.start();
            
            //client.create().forPath("/zk_test", "my_data".getBytes());
            
            //client.getData().forPath("/zk_test");            
            
            client.delete().forPath("/zk_test");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
