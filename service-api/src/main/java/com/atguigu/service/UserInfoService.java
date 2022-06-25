package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserInfo;

public interface UserInfoService  {

    /*根据手机号查询用户信息
     * @description:
     * @author: niuzp
     * @date: 2022/6/20 20:42
     **/
    UserInfo findByPhone(String phone);

 /*保存用户信息
  * @description:
  * @author: niuzp
  * @date: 2022/6/20 22:56
  **/
    void insert(UserInfo userInfo);
}
