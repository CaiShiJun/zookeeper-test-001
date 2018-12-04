package org.github.caishijun.zookeeperclient.test008;

/*
来源网址：https://www.cnblogs.com/wuxl360/p/5817549.html

2.6 队列管理
Zookeeper 可以处理两种类型的队列：

① 当一个队列的成员都聚齐时，这个队列才可用，否则一直等待所有成员到达，这种是同步队列。

② 队列按照 FIFO 方式进行入队和出队操作，例如实现生产者和消费者模型。

(1) 同步队列用 Zookeeper 实现的实现思路如下：

创建一个父目录 /synchronizing，每个成员都监控标志（Set Watch）位目录 /synchronizing/start 是否存在，然后每个成员都加入这个队列，加入队列的方式就是创建 /synchronizing/member_i 的临时目录节点，然后每个成员获取 / synchronizing 目录的所有目录节点，也就是 member_i。判断 i 的值是否已经是成员的个数，如果小于成员个数等待 /synchronizing/start 的出现，如果已经相等就创建 /synchronizing/start。

 */