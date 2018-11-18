package com.wust.service.impl;

import com.wust.distributedlock.redis.RedissLockUtil;
import com.wust.distributedlock.zookeeper.ZkLockUtil;
import com.wust.entity.Seckill;
import com.wust.entity.SuccessKilled;
import com.wust.mapper.SeckillMapper;
import com.wust.mapper.SuccessKilledMapper;
import com.wust.service.ISeckillDistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillDistributedServiceImpl implements ISeckillDistributedService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SuccessKilledMapper successKilledMapper;

    /**
     * 利用Redis实现分布式锁
     * 尝试获取锁，最多等待3秒，上锁以后20秒自动解锁（实际项目中推荐这种，以防出现死锁）
     * 这里根据预估秒杀人数，设定自动释放锁时间.
     * (Lcok锁与事务冲突的问题)：https://blog.52itstyle.com/archives/2952/
     * @param seckillId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public int startRedisLock(long seckillId, long userId) {

        //是否获取锁标识
        boolean res=false;

        try {
            //尝试获取锁
            res = RedissLockUtil.tryLock(seckillId + "", TimeUnit.SECONDS, 3, 20);

            if (res) {
                //获取锁成功
                //查询当前库存数
                Seckill currSeckill = seckillMapper.selectByPrimaryKey(seckillId);
                int currNumber = currSeckill.getNumber();
                if (currNumber > 0) {
                    //库存满足 可秒杀抢购
                    //插入秒杀成功数据
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState(new Byte("0"));
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    successKilledMapper.insert(killed);
                    //更新库存
                    currSeckill.setNumber(currNumber-1);
                    seckillMapper.updateByPrimaryKey(currSeckill);
                } else {
                    //库存不足,秒杀失败
                    return 1;
                }
            } else {
                //获取锁失败 秒杀失败
                return 2;
            }
        }catch (Exception e) {
            //未知异常
            e.printStackTrace();
        }finally {
            //释放锁
            if(res){
                RedissLockUtil.unlock(seckillId+"");
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public int startZkLock(long seckillId, long userId) {

        //是否获取锁标识
        boolean res=false;

        try {
            //使用zk分布式锁
            res = ZkLockUtil.acquire(3,TimeUnit.SECONDS);

            if (res) {
                //获取锁成功
                //查询当前库存数
                Seckill currSeckill = seckillMapper.selectByPrimaryKey(seckillId);
                int currNumber = currSeckill.getNumber();
                if (currNumber > 0) {
                    //库存满足 可秒杀抢购
                    //插入秒杀成功数据
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState(new Byte("0"));
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    successKilledMapper.insert(killed);
                    //更新库存
                    currSeckill.setNumber(currNumber-1);
                    seckillMapper.updateByPrimaryKey(currSeckill);
                } else {
                    //库存不足,秒杀失败
                    return 1;
                }
            } else {
                //获取锁失败 秒杀失败
                return 2;
            }
        }catch (Exception e) {
            //未知异常
            e.printStackTrace();
        }finally {
            //释放锁
            if(res){
                ZkLockUtil.release();
            }
        }
        return 0;
    }

    @Override
    @Transactional
    public int startDBForUpdateLockRank(long seckillId, long userId) {

        //查询库存,并实行行锁
        Seckill currSeckill = seckillMapper.selectByPrimaryKeyLockRank(seckillId);
        int currNumber = currSeckill.getNumber();
        if(currNumber>0){
            //库存满足 可秒杀抢购
            //插入秒杀成功数据
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState(new Byte("0"));
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
            //更新库存
            currSeckill.setNumber(currNumber-1);
            seckillMapper.updateByPrimaryKey(currSeckill);
        }else{
            //库存不足,秒杀失败
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional
    public int startDBLockTable(long seckillId, long userId) {

        //更新库存,并实行表锁
        int sqlResult = seckillMapper.updateByPrimaryKeyLockTable(seckillId);
        //System.out.println("更新库存结果--->"+sqlResult);
        if(sqlResult>0){
            //秒杀成功
            //插入秒杀成功数据
            SuccessKilled killed = new SuccessKilled();
            killed.setSeckillId(seckillId);
            killed.setUserId(userId);
            killed.setState(new Byte("0"));
            killed.setCreateTime(new Timestamp(new Date().getTime()));
            successKilledMapper.insert(killed);
        }else{
            //库存不足,秒杀失败
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional
    public int startDBOptimisticLock(long seckillId,long userId) {

        //查询库存
        Seckill currSeckill = seckillMapper.selectByPrimaryKey(seckillId);
        int currNumber = currSeckill.getNumber();
        if(currNumber>0){
            //库存满足 可秒杀抢购
            //利用乐观锁更新库存
            int sqlResult = seckillMapper.updateByPrimaryKeyOptimisticLock(currSeckill);
            System.out.println("更新库存结果--->"+sqlResult);
            if(sqlResult>0){
                //插入秒杀成功数据
                SuccessKilled killed = new SuccessKilled();
                killed.setSeckillId(seckillId);
                killed.setUserId(userId);
                killed.setState(new Byte("0"));
                killed.setCreateTime(new Timestamp(new Date().getTime()));
                successKilledMapper.insert(killed);
            }else{
                //更新库存失败
                return 2;
            }
        }else{
            //库存不足,秒杀失败
            return 1;
        }
        return 0;
    }
}
