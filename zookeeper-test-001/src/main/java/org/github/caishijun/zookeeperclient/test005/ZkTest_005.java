package org.github.caishijun.zookeeperclient.test005;

public class ZkTest_005 {
    public static void main(String[] args) {
        String zookeeperAddr_1 = "192.168.228.132:2181";
        String zookeeperAddr_2 = "192.168.228.133:2181";

        Runnable task1 = new Runnable(){
            public void run() {
                DistributedLock_005 lock = null;
                try {
                    lock = new DistributedLock_005(zookeeperAddr_1,"test1");
                    //lock = new DistributedLock("127.0.0.1:2182","test2");
                    lock.lock();
                    Thread.sleep(3000);
                    System.out.println("===Thread " + Thread.currentThread().getId() + " running");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(lock != null)
                        lock.unlock();
                }

            }

        };
        new Thread(task1).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        ConcurrentTask_005[] tasks = new ConcurrentTask_005[60];
        for(int i=0;i<tasks.length;i++){
            ConcurrentTask_005 task3 = new ConcurrentTask_005(){
                public void run() {
                    DistributedLock_005 lock = null;
                    try {
                        lock = new DistributedLock_005(zookeeperAddr_2,"test2");
                        lock.lock();
                        System.out.println("Thread " + Thread.currentThread().getId() + " running");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        lock.unlock();
                    }

                }
            };
            tasks[i] = task3;
        }
        new ConcurrentTest_005(tasks);
    }
}
