package org.github.caishijun.zookeeperclient.test002;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;

/**
 * 来源网址：https://www.cnblogs.com/wuxl360/p/5817524.html
 *
 * 列出组成员
 */
public class ListGroup_002 extends ConnectionWatcher_002 {
    public void list(String groupNmae) throws KeeperException, InterruptedException{
        String path ="/"+groupNmae;
        try {
            List<String> children = zk.getChildren(path, false);
            if(children.isEmpty()){
                System.out.printf("No memebers in group %s\n",groupNmae);
                System.exit(1);
            }
            for(String child:children){
                System.out.println(child);
            }
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist \n", groupNmae);
            System.exit(1);
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String hosts = "localhost:2181";       //hosts
        String groupName = "monkey";       //groupName

        ListGroup_002 listGroup = new ListGroup_002();
        listGroup.connect(hosts);
        listGroup.list(groupName);
        listGroup.close();
    }
}
