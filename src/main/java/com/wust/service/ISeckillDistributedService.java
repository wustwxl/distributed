package com.wust.service;

public interface ISeckillDistributedService {

    /*
    我们需要怎么样的分布式锁？
    1. 可以保证在分布式部署的应用集群中，同一个方法在同一时间只能被一台机器上的一个线程执行。
    2. 这把锁要是一把可重入锁（避免死锁）
    3. 这把锁最好是一把阻塞锁（根据业务需求考虑要不要这条）
    4. 这把锁最好是一把公平锁（根据业务需求考虑要不要这条）
    5. 有高可用的获取锁和释放锁功能
    6. 获取锁和释放锁的性能要好
     */

    /**
     * 利用 redis 实现分布式锁
     */
    int startRedisLock(long seckillId,long userId);

    /**
     * 利用 zookeeper 实现分布式锁
     */
    int startZkLock(long seckillId,long userId);

    /**
     * 利用 数据库悲观锁 实现分布式锁
     * 方法一:行锁
     *
     * 使用ForUpdate添加排它锁(此处为行锁)
     * 注意事项!!
     * InnoDB行锁是通过给索引上的索引项加锁来实现的
     * InnoDB这种行锁实现特点意味着：只有通过索引条件检索数据，InnoDB才使用行级锁，否则，InnoDB将使用表锁
     */
    int startDBForUpdateLockRank(long seckillId,long userId);

    /**
     * 利用 数据库悲观锁 实现分布式锁
     * 方法二:表锁
     */
    int startDBLockTable(long seckillId,long userId);

    /**
     * 数据库乐观锁 更新数据时可用于保证幂等性
     */
    int startDBOptimisticLock(long seckillId,long userId);
}
