package org.github.caishijun.zookeeperclient.test009;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * FIFOQueue
 */
public class FIFOQueue_009 extends TestMainClient_009 {
    public static final Logger logger = Logger.getLogger(FIFOQueue_009.class);

    /**
     * Constructor
     *
     * @param connectString
     * @param root
     */
    FIFOQueue_009(String connectString, String root) {
        super(connectString);
        this.root = root;
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                logger.error(e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 生产者
     *
     * @param i
     * @return
     */
    boolean produce(int i) throws KeeperException, InterruptedException {
        ByteBuffer b = ByteBuffer.allocate(4);
        byte[] value;
        b.putInt(i);
        value = b.array();
        zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT_SEQUENTIAL);
        return true;
    }


    /**
     * 消费者
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    int consume() throws KeeperException, InterruptedException {
        int retvalue = -1;
        Stat stat = null;
        while (true) {
            synchronized (mutex) {
                List<String> list = zk.getChildren(root, true);
                if (list.size() == 0) {
                    mutex.wait();
                } else {
                    Integer min = new Integer(list.get(0).substring(7));
                    for (String s : list) {
                        Integer tempValue = new Integer(s.substring(7));
                        if (tempValue < min) {
                            min = tempValue;
                        }
                    }
                    byte[] b = zk.getData(root + "/element" + min, false, stat);
                    zk.delete(root + "/element" + min, 0);
                    ByteBuffer buffer = ByteBuffer.wrap(b);
                    retvalue = buffer.getInt();
                    return retvalue;
                }
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        super.process(event);
    }

    public static void main(String args[]) {
        //启动Server
        //TestMainServer.start();
        //String connectString = "localhost:"+TestMainServer.CLIENT_PORT;
        String connectString = "localhost:2181";

        FIFOQueue_009 q = new FIFOQueue_009(connectString, "/app1");
        int i;
        Integer max = new Integer(5);

        System.out.println("Producer");
        for (i = 0; i < max; i++) {
            try {
                q.produce(10 + i);
            } catch (KeeperException e) {
                logger.error(e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }

        for (i = 0; i < max; i++) {
            try {
                int r = q.consume();
                System.out.println("Item: " + r);
            } catch (KeeperException e) {
                i--;
                logger.error(e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }
}