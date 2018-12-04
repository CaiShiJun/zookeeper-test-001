package org.github.caishijun.zookeeperclient.test009;

/*
来源网址：https://www.cnblogs.com/wuxl360/p/5817549.html


(2) FIFO 队列用 Zookeeper 实现思路如下：

实现的思路也非常简单，就是在特定的目录下创建 SEQUENTIAL 类型的子目录 /queue_i，这样就能保证所有成员加入队列时都是有编号的，出队列时通过 getChildren( ) 方法可以返回当前所有的队列中的元素，然后消费其中最小的一个，这样就能保证 FIFO。

下面是生产者和消费者这种队列形式的示例代码

清单 5 FIFOQueue 代码

 */