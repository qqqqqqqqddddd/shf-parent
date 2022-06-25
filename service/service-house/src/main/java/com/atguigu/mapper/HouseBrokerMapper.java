package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {

    List<HouseBroker> findHouseBrokerListByHouseId(Long houseId);


    HouseBroker getByHouseIdAndBrokerId(@Param("houseId") Long houseId,@Param("brokerId") Long brokerId);
}
