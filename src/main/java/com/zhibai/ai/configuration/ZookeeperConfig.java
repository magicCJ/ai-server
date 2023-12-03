//package com.xunbai.ai.configuration;
//
//import org.apache.curator.RetryPolicy;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author: kanson
// * @desc:
// * @version: 1.0
// * @time: 2023/05/27 06:23
// */
//@Configuration
//public class ZookeeperConfig {
//
//    @Value("${zookeeper.connectString}")
//    private String connectString;
//
//    @Value("${zookeeper.sessionTimeoutMs}")
//    private int sessionTimeoutMs;
//
//    @Value("${zookeeper.connectionTimeoutMs}")
//    private int connectionTimeoutMs;
//
//    @Value("${zookeeper.baseSleepTimeMs}")
//    int baseSleepTimeMs;
//
//    @Value("${zookeeper.maxRetries}")
//    int maxRetries;
//
//    @Bean(initMethod = "start", destroyMethod = "close")
//    public CuratorFramework curatorFramework() {
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
//        return CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
//    }
//
//}
//
