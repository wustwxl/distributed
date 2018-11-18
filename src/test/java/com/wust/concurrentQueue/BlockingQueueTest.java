package com.wust.concurrentQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("堵塞队列详解");
        System.out.println("https://blog.csdn.net/tantion/article/details/82148026");

        /*
        BlockingQueue接口,其实现类有以下7种.......
        见博客
         */
        //堵塞队列 大小为三
        //高并发建议使用LinkedBlockingDeque,其生产者端和消费者端分别采用独立的锁控制数据同步
        BlockingQueue<String> bq = new LinkedBlockingDeque<>(3);
        bq.offer("1");
        bq.offer("2");
        bq.offer("3");
        //System.out.println(bq.poll());
        bq.offer("4",2, TimeUnit.SECONDS);

        System.out.println(bq.poll());
        System.out.println(bq.poll());
        System.out.println(bq.poll());
        System.out.println(bq.poll());


    }

}
