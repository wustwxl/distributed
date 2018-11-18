package com.wust.mapper;

import com.wust.entity.Seckill;
import com.wust.entity.SeckillExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeckillMapper {
    int countByExample(SeckillExample example);

    int deleteByExample(SeckillExample example);

    int deleteByPrimaryKey(Long seckillId);

    int insert(Seckill record);

    int insertSelective(Seckill record);

    List<Seckill> selectByExample(SeckillExample example);

    Seckill selectByPrimaryKey(Long seckillId);

    int updateByExampleSelective(@Param("record") Seckill record, @Param("example") SeckillExample example);

    int updateByExample(@Param("record") Seckill record, @Param("example") SeckillExample example);

    int updateByPrimaryKeySelective(Seckill record);

    int updateByPrimaryKey(Seckill record);

    //行锁
    Seckill selectByPrimaryKeyLockRank(Long seckillId);

    //表锁
    int updateByPrimaryKeyLockTable(Long seckillId);

    //乐观锁
    int updateByPrimaryKeyOptimisticLock(Seckill record);

}