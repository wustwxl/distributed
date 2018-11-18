package com.wust.distributedlock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * zookeeper 分布式锁
 */
public class ZkLockUtil{

	private static String address = "IP:2181";

	//利用ZooKeeper客户端框架Curator
	/*
	Curator包含了几个包：
	curator-framework：对zookeeper的底层api的一些封装
	curator-client：提供一些客户端的操作，例如重试策略等
	curator-recipes：封装了一些高级特性，如：Cache事件监听、选举、分布式锁、分布式计数器、分布式Barrier等
	 */
	public static CuratorFramework client = null;
	
	static{
		//重试策略,baseSleepTimeMs参数代表两次连接的等待时间,maxRetries参数表示最大的尝试连接次数
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		//使用静态工程方法创建客户端
        client = CuratorFrameworkFactory.newClient(address, retryPolicy);
        /*
        newClient静态工厂方法包含四个主要参数：
        connectionString	服务器列表，格式host1:port1,host2:port2,...
        retryPolicy	重试策略,内建有四种重试策略,也可以自行实现RetryPolicy接口
        sessionTimeoutMs	会话超时时间，单位毫秒，默认60000ms
        connectionTimeoutMs	连接创建超时时间，单位毫秒，默认60000ms
         */
        client.start();
	}

	/**
     * 私有的默认构造函数，保证外界无法直接实例化
     */
    private ZkLockUtil(){};
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     * 针对一件商品实现，多件商品同时秒杀建议实现一个map
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         * 参考：http://ifeve.com/zookeeper-lock/
         * 这里建议 new 一个
         */

        /*
        几种锁方案
        InterProcessMutex：分布式可重入排它锁
        InterProcessSemaphoreMutex：分布式排它锁
        InterProcessReadWriteLock：分布式读写锁
        InterProcessMultiLock：将多个锁作为单个实体管理的容器
         */
    	private  static InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock"); 
    }

    /*
    InterProcessMutex 原理总结
    InterProcessMutex通过在zookeeper的某路径节点下创建临时序列节点来实现分布式锁，
    即每个线程（跨进程的线程）获取同一把锁前，都需要在同样的路径下创建一个节点，节点名字由uuid + 递增序列组成。
    而通过对比自身的序列数是否在所有子节点的第一位，来判断是否成功获取到了锁。
    当获取锁失败时，它会添加watcher来监听前一个节点的变动情况，然后进行等待状态。
    直到watcher的事件生效将自己唤醒，或者超时时间异常返回。
     */
    public static InterProcessMutex getMutex(){
        return SingletonHolder.mutex;
    }

	/**
	 * 获取锁 抢夺时，如果出现堵塞，会在超过该时间后，返回false
	 * @param time 入参传入超时时间
	 * @param unit 入参传入超时时间单位
	 * @return
	 */
    public static boolean acquire(long time, TimeUnit unit){
    	try {
			return getMutex().acquire(time,unit);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    //释放锁
    public static void release(){
    	try {
			getMutex().release();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}  
