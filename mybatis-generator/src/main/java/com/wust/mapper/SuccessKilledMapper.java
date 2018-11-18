package com.wust.mapper;

import com.wust.entity.SuccessKilled;
import com.wust.entity.SuccessKilledExample;
import com.wust.entity.SuccessKilledKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledMapper {
    int countByExample(SuccessKilledExample example);

    int deleteByExample(SuccessKilledExample example);

    int deleteByPrimaryKey(SuccessKilledKey key);

    int insert(SuccessKilled record);

    int insertSelective(SuccessKilled record);

    List<SuccessKilled> selectByExample(SuccessKilledExample example);

    SuccessKilled selectByPrimaryKey(SuccessKilledKey key);

    int updateByExampleSelective(@Param("record") SuccessKilled record, @Param("example") SuccessKilledExample example);

    int updateByExample(@Param("record") SuccessKilled record, @Param("example") SuccessKilledExample example);

    int updateByPrimaryKeySelective(SuccessKilled record);

    int updateByPrimaryKey(SuccessKilled record);
}