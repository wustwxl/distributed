package com.wust.service;

public interface ISeckillService {
    /**
     * 删除秒杀售卖商品记录
     * @param seckillId
     * @return
     */
    void deleteSeckill(long seckillId);

    /**
     * 查询秒杀售卖商品
     * @param seckillId
     * @return
     */
    Long getSeckillCount(long seckillId);

}
