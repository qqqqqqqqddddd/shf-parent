package com.atguigu.servvice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;

    @Override
    protected BaseMapper<Community> getEntityMapper() {
        return communityMapper;
    }

    @Override
    public void delete(Long id) {
        Integer count = houseMapper.findCountByCommunityId(id);
        if (count>0){

            throw new RuntimeException("小区内有房源,删除失败");
        }
        super.delete(id);
    }
    @Override
    public List<Community> findAll(){
        return communityMapper.findAll();
    }
}
