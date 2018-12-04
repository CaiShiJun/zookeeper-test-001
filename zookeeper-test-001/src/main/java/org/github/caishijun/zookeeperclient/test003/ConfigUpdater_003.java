package org.github.caishijun.zookeeperclient.test003;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

/**
 * 用于随机更新ZooKeeper中的属性
 */
public class ConfigUpdater_003 {

    public static final String PATH = "/config";

    private ActiveKeyValueStore_003 store;
    private Random random = new Random();

    public ConfigUpdater_003(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore_003();
        store.connect(hosts);
    }

    public void run() throws InterruptedException, KeeperException {
        while (true) {
            String value = random.nextInt(100) + "";
            store.write(PATH, value);
            System.out.printf("Set %s to %s\n", PATH, value);
            TimeUnit.SECONDS.sleep(random.nextInt(100));

        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String hosts = "localhost:2181";       //hosts

        ConfigUpdater_003 configUpdater = new ConfigUpdater_003(hosts);
        configUpdater.run();
    }
}