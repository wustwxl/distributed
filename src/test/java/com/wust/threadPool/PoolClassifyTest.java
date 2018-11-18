package com.wust.threadPool;

import org.junit.Test;

import java.util.concurrent.*;

public class PoolClassifyTest {

    //psvm快捷键
    public static void main(String[] args) throws InterruptedException {

        //线程池有四种,其底层实现均是由ThreadPoolExecutor实现的
        System.out.println("线程池分类详解");
        System.out.println("https://www.jianshu.com/p/ae67972d1156");
    }

    @Test
    public void cahedThreadPoolTest() {

        //可缓存线程池 CachedThreadPool()
        //这种线程池内部没有核心线程，线程的数量是有没限制的。
        //在创建任务时，若有空闲的线程时则复用空闲的线程，若没有则新建线程。
        //没有工作的线程（闲置状态）在超过了60S还不做事，就会销毁
        ExecutorService cahedThreadPool = Executors.newCachedThreadPool();
        for (int i=0 ; i<100; i++){
            //采用匿名内部类创建任务
            cahedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程名:"+Thread.currentThread().getName());
                }
            });
        }
        cahedThreadPool.shutdown();
    }

    @Test
    public void fixedThreadPoolTest() {

        //定长线程池 FixedThreadPool()
        //该线程池的最大线程数等于核心线程数，所以在默认情况下，该线程池的线程不会因为闲置状态超时而被销毁。
        //如果当前线程数小于核心线程数，并且也有闲置线程的时候提交了任务，这时也不会去复用之前的闲置线程，会创建新的线程去执行任务。
        //如果当前执行任务数大于了核心线程数，大于的部分就会进入队列等待。等着有闲置的线程来执行这个任务。

        // nThreads => 最大线程数即maximumPoolSize
        ExecutorService fixedThreadPool= Executors.newFixedThreadPool(5 );

        for (int i=0 ; i<100; i++){
            //采用匿名内部类创建任务
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程名:"+Thread.currentThread().getName());
                }
            });
        }
        fixedThreadPool.shutdown();
    }

    @Test
    public void scheduledThreadPoolTest() {

        //定时线程池 ScheduledThreadPool()
        //该线程池不仅设置了核心线程数，最大线程数也是Integer.MAX_VALUE。
        //这个线程池是上述4个中为唯一个有延迟执行和周期执行任务的线程池。

        //nThreads => 最大线程数即maximumPoolSize
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        for (int i=0 ; i<100; i++) {
            //采用匿名内部类创建任务,两秒后开始执行任务(延迟执行)
            scheduledThreadPool.schedule(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程名:"+Thread.currentThread().getName());
                }
            }, 2, TimeUnit.SECONDS);
        }
        scheduledThreadPool.shutdown();

        /*
        //周期执行任务

        //延迟3秒后执行任务，从开始执行任务这个时候开始计时，每7秒执行一次不管执行任务需要多长的时间。
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //....
            }
        },3, 7, TimeUnit.SECONDS);

        //延迟3秒后执行任务，从任务完成时这个时候开始计时，7秒后再执行，再等完成后计时7秒再执行
        //也就是说这里的循环执行任务的时间点是从上一个任务完成的时候。
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //....
            }
        },3, 7, TimeUnit.SECONDS);
        */

    }

    @Test
    public void singleThreadPoolTest() {

        //单例线程池 SingleThreadPool()
        //有且仅有一个工作线程执行任务
        //所有任务按照指定顺序执行，即遵循队列的入队出队规则
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

        for (int i=0 ; i<100; i++) {
            //采用匿名内部类创建任务
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程名:"+Thread.currentThread().getName());
                }
            });
        }
        singleThreadPool.shutdown();
    }

}
