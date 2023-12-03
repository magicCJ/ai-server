//package com.xunbai.ai.util;
//
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.zookeeper.CreateMode;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author: kanson
// * @desc:
// * @version: 1.0
// * @time: 2023/05/27 06:24
// */
//@Service
//public class ZookeeperUtil {
//    @Resource
//    private CuratorFramework curatorFramework;
//
//    public void createNode(String path, String data) throws Exception {
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());
//    }
//
//    public String getNodeData(String path) throws Exception {
//        return new String(curatorFramework.getData().forPath(path));
//    }
//
//    public void updateNodeData(String path, String data) throws Exception {
//        curatorFramework.setData().forPath(path, data.getBytes());
//    }
//
//    public void deleteNode(String path) throws Exception {
//        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
//    }
//
//    public List<String> getChildren(String path) throws Exception {
//        return curatorFramework.getChildren().forPath(path);
//    }
//}
