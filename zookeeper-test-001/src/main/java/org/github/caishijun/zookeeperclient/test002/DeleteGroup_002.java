package org.github.caishijun.zookeeperclient.test002;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;

/**
 * 来源网址：https://www.cnblogs.com/wuxl360/p/5817524.html
 *
 * 删除一个组及其所有成员
 *
 * 如果所提供的版本号与znode的版本号一致，ZooKeeper会删除这个znode。
 * 这是一种乐观的加锁机制，使客户端能够检测出对znode的修改冲突。
 * 通过将版本号设置为-1，可以绕过这个版本检测机制，不管znode的版本号是什么而直接将其删除。
 * ZooKeeper不支持递归的删除操作，因此在删除父节点之前必须先删除子节点。
 *
 * [root@hadoop code]# java org.zk.DeleteGroup localhost zoo
 * ……
 * [root@hadoop code]# java org.zk.ListGroup localhost zoo
 */
public class DeleteGroup_002 extends ConnectionWatcher_002{
    public void delete(String groupName) throws InterruptedException, KeeperException{
        String path="/"+groupName;
        List<String> children;
        try {
            children = zk.getChildren(path, false);
            for(String child:children){
                zk.delete(path+"/"+child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }



    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        String hosts = "localhost:2181";       //hosts
        String groupName = "monkey";       //groupName

        DeleteGroup_002 deleteGroup = new DeleteGroup_002();
        deleteGroup.connect(hosts);
        deleteGroup.delete(groupName);
        deleteGroup.close();
    }
}