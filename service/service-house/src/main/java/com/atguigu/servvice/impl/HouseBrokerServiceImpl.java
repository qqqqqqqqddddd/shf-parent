package com.atguigu.servvice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseBroker;
import com.atguigu.mapper.HouseBrokerMapper;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = HouseBrokerService.class)
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {
    @Autowired
    private HouseBrokerMapper houseBrokerMapper;
    @Override
    protected BaseMapper<HouseBroker> getEntityMapper() {
        return  houseBrokerMapper;
    }

    @Override
    public List<HouseBroker> findHouseBrokerListByHouseId(Long houseId) {
        return houseBrokerMapper.findHouseBrokerListByHouseId(houseId);
    }

    @Override
    public HouseBroker getByHouseIdAndBrokerId(Long houseId, Long brokerId) {
        return houseBrokerMapper.getByHouseIdAndBrokerId(houseId,brokerId);
    }
}
