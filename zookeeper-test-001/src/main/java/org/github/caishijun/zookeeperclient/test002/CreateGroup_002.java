package org.github.caishijun.zookeeperclient.test002;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 来源网址：https://www.cnblogs.com/wuxl360/p/5817524.html
 *
 * 在ZooKeeper中新建表示组的Znode。
 *
 * [root@hadoop code]# javac -d ./classes CreateGroup.java
 * [root@hadoop code]# java org.zk.CreateGroup localhost:2181 zoo
 */
public class CreateGroup_002 implements Watcher{
    private static final int SESSION_TIMEOUT=5000;

    private ZooKeeper zk;
    private CountDownLatch connectedSignal=new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {
        if(event.getState()==KeeperState.SyncConnected){
            connectedSignal.countDown();
        }
    }

    private void close() throws InterruptedException {
        zk.close();
    }

    private void create(String groupName) throws KeeperException, InterruptedException {
        String path="/"+groupName;
        if(zk.exists(path, false)== null){
            zk.create(path, null/*data*/, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("Created:"+path);
    }

    private void connect(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }



    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String hosts = "localhost:2181";       //hosts
        String groupName = "monkey";       //groupName

        CreateGroup_002 createGroup = new CreateGroup_002();
        createGroup.connect(hosts);
        createGroup.create(groupName);
        createGroup.close();
    }
}
