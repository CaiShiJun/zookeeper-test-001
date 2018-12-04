package org.github.caishijun.zookeeperclient.test003;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 来源网址：https://www.cnblogs.com/wuxl360/p/5817524.html
 *
 * 用于等待建立与ZooKeeper连接的辅助类。
 */
public class ConnectionWatcher_003 implements Watcher{
    private static final int SESSION_TIMEOUT=5000;

    protected ZooKeeper zk;
    CountDownLatch connectedSignal=new CountDownLatch(1);
    public void connect(String host) throws IOException, InterruptedException{
        zk=new ZooKeeper(host, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }
    @Override
    public void process(WatchedEvent event) {
        if(event.getState()==KeeperState.SyncConnected){
            connectedSignal.countDown();
        }
    }
    public void close() throws InterruptedException{
        zk.close();
    }

}