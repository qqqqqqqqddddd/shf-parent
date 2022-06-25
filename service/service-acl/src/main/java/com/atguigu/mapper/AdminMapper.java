package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
   /*
    * @description:查询全部的管理员
    * @author: niuzp
    * @date: 2022/6/17 15:39
    **/
    List<Admin>  findAll();

    Admin getByUsername(String username);
}
