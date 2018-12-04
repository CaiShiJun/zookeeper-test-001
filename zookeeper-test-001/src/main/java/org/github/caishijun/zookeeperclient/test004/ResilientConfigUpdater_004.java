package org.github.caishijun.zookeeperclient.test004;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ResilientConfigUpdater_004 extends ConnectionWatcher_004{
    public static final String PATH="/config";
    private ChangedActiveKeyValueStore_004 store;
    private Random random=new Random();

    public ResilientConfigUpdater_004(String hosts) throws IOException, InterruptedException {
        store=new ChangedActiveKeyValueStore_004();
        store.connect(hosts);
    }
    public void run() throws InterruptedException, KeeperException{
        while(true){
            String value=random.nextInt(100)+"";
            store.write(PATH,value);
            System.out.printf("Set %s to %s\n",PATH,value);
            TimeUnit.SECONDS.sleep(random.nextInt(10));
        }
    }



    public static void main(String[] args) throws Exception {
        while(true){
            try {
                ResilientConfigUpdater_004 configUpdater = new ResilientConfigUpdater_004(args[0]);
                configUpdater.run();
            }catch (KeeperException.SessionExpiredException e) {
                // start a new session
            }catch (KeeperException e) {
                // already retried ,so exit
                e.printStackTrace();
                break;
            }
        }
    }

    /*
    在这段代码中没有对KeepException.SeeionExpiredException异常进行重试，因为一个会话过期 时，ZooKeeper对象会进入CLOSED状态，此状态下它不能进行重试连接。我们只能将这个异常简单抛出并让拥有着创建一个新实例，以重试整个 write()方法。一个简单的创建新实例的方法是创建一个新的ResilientConfigUpdater用于恢复过期会话。

    处理会话过期的另一种方法是在观察中（在这个例子中应该是ConnectionWatcher）寻找类型为ExpiredKeepState，然后 再找到的时候创建一个新连接。即使我们收到KeeperException.SessionExpiredEception异常，这种方法还是可以让我们 在write（）方法内不断重试，因为连接最终是能够重新建立的。不管我们采用何种机制从过期会话中恢复，重要的是，这种不同于连接丢失的故障类型，需要 进行不同的处理。

    注意：实际上,这里忽略了另一种故障模式。当ZooKeeper对象被创建时，他会尝试连接另一个ZooKeeper服务器。如果连接失败或超时， 那么他会尝试连接集合体中的另一台服务器。如果在尝试集合体中的所有服务器之后仍然无法建立连接，它会抛出一个IOException异常。由于所有的 ZooKeeper服务器都不可用的可能性很小，所以某些应用程序选择循环重试操作，直到ZooKeeper服务为止。

    这仅仅是一种重试处理策略，还有许多其他处理策略，例如使用“指数返回”，每次将重试的间隔乘以一个常数。Hadoop内核中 org.apache.hadoop.io.retry包是一组工具，用于可以重用的方式将重试逻辑加入代码，因此他对于构建ZooKeeper应用非常 有用。
     */
}
