package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.UserInfo;
import com.atguigu.mapper.UserInfoMapper;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl  implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public UserInfo findByPhone(String phone) {
        return userInfoMapper.findByPhone(phone);
    }

    @Override
    public void insert(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

}
