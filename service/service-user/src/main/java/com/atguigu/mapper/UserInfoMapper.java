package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserInfo;

public interface UserInfoMapper {

    UserInfo findByPhone(String phone);

    void insert(UserInfo userInfo);
}
