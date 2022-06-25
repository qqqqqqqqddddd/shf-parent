package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Community;

import java.util.List;

public interface CommunityMapper extends BaseMapper<Community> {
    /*
     * @description:查询所有小区
     * @author: niuzp
     * @date: 2022/6/16 10:50
     **/
    List<Community> findAll();
}
