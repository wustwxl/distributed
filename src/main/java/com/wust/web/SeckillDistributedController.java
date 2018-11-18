package com.wust.web;


import com.wust.redis.RedisUtil;
import com.wust.service.ISeckillDistributedService;
import com.wust.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seckillDistributed")
public class SeckillDistributedController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SeckillDistributedController.class);

	private static int corePoolSize = Runtime.getRuntime().availableProcessors();
	//调整队列数 拒绝服务
	/*
	https://www.jianshu.com/p/ae67972d1156
	int corePoolSize：该线程池中核心线程数最大值
	int maximumPoolSize： 该线程池中线程总数最大值
	long keepAliveTime：该线程池中非核心线程闲置超时时长
	TimeUnit unit：keepAliveTime的单位
	BlockingQueue workQueue：该线程池中的任务队列：维护着等待执行的Runnable对象
	 */
	private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(10000));

	@Autowired
	private ISeckillService seckillService;
	@Autowired
	private ISeckillDistributedService seckillDistributedService;

	@Autowired
	private RedisUtil redisUtil;
	
	//Redission分布式锁
	@GetMapping("/RedisLock/{seckillId}")
	public void startRedisLock(@PathVariable("seckillId") long seckillId){

		final long killId =  seckillId;
		//开始秒杀前还原数据库数据
		seckillService.deleteSeckill(seckillId);
		LOGGER.info("开始秒杀");
		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int result = seckillDistributedService.startRedisLock(killId, userId);
					LOGGER.info("用户:{}秒杀结果{}",userId,result);
				}
			};
			executor.execute(task);
		}
		try {
			Thread.sleep(30000);
			Long  seckillCount = seckillService.getSeckillCount(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//zookeeper分布式锁
	@GetMapping("/ZkLock/{seckillId}")
	public void startZkLock(@PathVariable("seckillId") long seckillId){
		final long killId =  seckillId;
		//开始秒杀前还原数据库数据
		seckillService.deleteSeckill(seckillId);
		LOGGER.info("开始秒杀");

		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int result = seckillDistributedService.startZkLock(killId, userId);
					LOGGER.info("用户:{}秒杀结果{}",userId,result);
				}
			};
			executor.execute(task);
		}
		try {
			Thread.sleep(30000);
			Long  seckillCount = seckillService.getSeckillCount(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//数据库行锁分布式锁
	@GetMapping("/RankLock/{seckillId}")
	public void startDBLockRank(@PathVariable("seckillId") long seckillId){
		final long killId =  seckillId;
		//开始秒杀前还原数据库数据
		seckillService.deleteSeckill(seckillId);
		LOGGER.info("开始秒杀");

		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int result = seckillDistributedService.startDBForUpdateLockRank(killId, userId);
					LOGGER.info("用户:{}秒杀结果{}",userId,result);
				}
			};
			executor.execute(task);
		}
		try {
			Thread.sleep(30000);
			Long  seckillCount = seckillService.getSeckillCount(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//数据库表锁分布式锁
	@GetMapping("/TableLock/{seckillId}")
	public void startDBLockTable(@PathVariable("seckillId") long seckillId){
		final long killId =  seckillId;
		//开始秒杀前还原数据库数据
		seckillService.deleteSeckill(seckillId);
		LOGGER.info("开始秒杀");

		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int result = seckillDistributedService.startDBLockTable(killId, userId);
					LOGGER.info("用户:{}秒杀结果{}",userId,result);
				}
			};
			executor.execute(task);
		}
		try {
			Thread.sleep(30000);
			Long  seckillCount = seckillService.getSeckillCount(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//数据库乐观锁分布式锁
	@GetMapping("/OptimisticLock/{seckillId}")
	public void startDBLockOptimistic(@PathVariable("seckillId") long seckillId){
		final long killId =  seckillId;
		//开始秒杀前还原数据库数据
		seckillService.deleteSeckill(seckillId);
		LOGGER.info("开始秒杀");

		for(int i=0;i<1000;i++){
			final long userId = i;
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int result = seckillDistributedService.startDBOptimisticLock(killId, userId);
					LOGGER.info("用户:{}秒杀结果{}",userId,result);
				}
			};
			executor.execute(task);
		}
		try {
			Thread.sleep(30000);
			Long  seckillCount = seckillService.getSeckillCount(seckillId);
			LOGGER.info("一共秒杀出{}件商品",seckillCount);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
