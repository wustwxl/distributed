package com.wust.concurrentQueue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NonBlockingQueueTest {

    //psvm快捷键
    public static void main(String[] args) {

        System.out.println("非堵塞队列详解");
        System.out.println("https://blog.csdn.net/v123411739/article/details/79561458");

        //默认构造函数-无界线程安全队列
        //入队和出队操作均采用CAS(CAS:比较和更新,乐观锁的一种实现方式)更新
        //CAS有三个操作数,内存值V,旧的预期值A,要修改的新值B,当且仅当A和V相等时,才讲内存值V进行更新为B
        //实现原理:CAS通过调用JNI的代码实现的
        //缺点:ABA问题,CPU开销,只能保证一个共享变量的原子操作
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        //入队
        clq.add("1");
        clq.add("2");
        clq.add("3");
        //出队
        clq.poll();//出队,移除队列中元素
        System.out.println("poll()操作后队列大小:"+clq.size());
        clq.peek();//出队,不移除队列中元素,仅显示
        System.out.println("peek()操作后队列大小:"+clq.size());
    }

}
