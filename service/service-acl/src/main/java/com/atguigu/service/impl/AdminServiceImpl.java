package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    protected BaseMapper<Admin> getEntityMapper() {
        return adminMapper;
    }

    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUsername(username);
    }
}
