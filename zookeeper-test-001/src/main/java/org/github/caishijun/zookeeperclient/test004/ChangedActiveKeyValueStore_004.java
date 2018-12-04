package org.github.caishijun.zookeeperclient.test004;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ChangedActiveKeyValueStore_004 extends ConnectionWatcher_004 {
    private static final Charset CHARSET=Charset.forName("UTF-8");
    private static final int MAX_RETRIES = 5;       //重试的最大次数MAX_RETRIES
    private static final long RETRY_PERIOD_SECONDS = 5;     //两次重试之间的间隔RETRY_PERIOD_SECONDS

    /*
        在该类中，对前面ActiveKeyValueStore_003的write()进行了修改,该版本的wirte()能够循环执行重试。
        其中设置了重试的最大次数MAX_RETRIES和两次重试之间的间隔RETRY_PERIOD_SECONDS。
     */
    public void write(String path,String value) throws InterruptedException, KeeperException{
        int retries=0;
        while(true){
            try {
                Stat stat = zk.exists(path, false);
                if(stat==null){
                    zk.create(path, value.getBytes(CHARSET),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }else{
                    zk.setData(path, value.getBytes(CHARSET),stat.getVersion());
                }
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                if(retries++==MAX_RETRIES){
                    throw e;
                }
                //sleep then retry
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }
    }
    public String read(String path,Watcher watch) throws KeeperException, InterruptedException{
        byte[] data = zk.getData(path, watch, null);
        return new String(data,CHARSET);
    }
}