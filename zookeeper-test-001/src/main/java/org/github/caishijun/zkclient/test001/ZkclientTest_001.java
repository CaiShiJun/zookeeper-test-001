package org.github.caishijun.zkclient.test001;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * 来源网址：https://blog.csdn.net/qq_26641781/article/details/80886831
 */
public class ZkclientTest_001 {
    public static void main(String[] args) throws Exception{
        //String connectString = "192.168.0.183:2181";
        String connectString = "localhost:2181";

        ZkClient zkClient = new ZkClient(connectString);//建立连接
        zkClient.create("/root","mydata", CreateMode.PERSISTENT);//创建目录并写入数据
        String data=zkClient.readData("/root");
        System.out.println(data);
        //zkClient.delete("/root");//删除目录
        //zkClient.deleteRecursive("/root");//递归删除节目录
    }
}
