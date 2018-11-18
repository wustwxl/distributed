package com.wust.threadPool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    //psvm快捷键
    public static void main(String[] args) throws InterruptedException {

        //线程池有四种,其底层实现均是由ThreadPoolExecutor实现的
        System.out.println("线程池详解");
        System.out.println("https://blog.csdn.net/qq_25806863/article/details/71126867");

        //ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(3));

        for (int i=0 ; i<5; i++){
            //执行任务
            executor.execute(new TestThread());
        }
        //关闭线程池
        executor.shutdown();
    }
}

class TestThread implements Runnable{

    @Override
    public void run() {
        System.out.println("当前线程名:"+Thread.currentThread().getName());
    }
}