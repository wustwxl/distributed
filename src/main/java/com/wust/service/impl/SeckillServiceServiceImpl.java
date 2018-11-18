package com.wust.service.impl;

import com.wust.entity.Seckill;
import com.wust.entity.SuccessKilled;
import com.wust.entity.SuccessKilledExample;
import com.wust.mapper.SeckillMapper;
import com.wust.mapper.SuccessKilledMapper;
import com.wust.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillServiceServiceImpl implements ISeckillService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SuccessKilledMapper successKilledMapper;

    @Override
    public void deleteSeckill(long seckillId) {

        //还原库存
        Seckill currSeckill = seckillMapper.selectByPrimaryKey(seckillId);
        currSeckill.setNumber(100);
        seckillMapper.updateByPrimaryKey(currSeckill);

        //删除已秒杀数据
        SuccessKilledExample example = new SuccessKilledExample();
        SuccessKilledExample.Criteria criteria = example.createCriteria(); //构造自定义查询条件
        criteria.andSeckillIdEqualTo(seckillId);
        successKilledMapper.deleteByExample(example);
    }

    @Override
    public Long getSeckillCount(long seckillId) {
        SuccessKilledExample example = new SuccessKilledExample();
        SuccessKilledExample.Criteria criteria = example.createCriteria(); //构造自定义查询条件
        criteria.andSeckillIdEqualTo(seckillId);
        Long num = Long.valueOf(successKilledMapper.countByExample(example));
        return num;
    }
}
