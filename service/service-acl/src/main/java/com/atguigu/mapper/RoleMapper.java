package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    /*
     * @description:查询所有角色
     * @author: niuzp
     * @date: 2022/6/11 16:32
     **/
    List<Role> findAll();

}
