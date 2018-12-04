package org.github.caishijun.zookeeperclient.test002;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 来源网址：https://www.cnblogs.com/wuxl360/p/5817524.html
 *
 * 将成员加入组的程序
 *
 * JoinGroup的代码与CreateGroup非常相似，在它的join()方法中，创建短暂znode，作为组znode的子节点，然后通过 休眠来模拟正在做某种工作，直到该进程被强行终止。
 * 接着，你会看到随着进程终止，这个短暂znode被ZooKeeper删除。
 */
public class JoinGroup_002 extends ConnectionWatcher_002{
    public void join(String groupName,String memberName) throws KeeperException, InterruptedException{
        String path="/"+groupName+"/"+memberName;
        String createdPath=zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Created:"+createdPath);
    }



    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        String hosts = "localhost:2181";       //hosts
        String groupName = "monkey";       //groupName
        String memberName = "monkey001";

        JoinGroup_002 joinGroup = new JoinGroup_002();
        joinGroup.connect(hosts);
        joinGroup.join(groupName, memberName);

        //stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}