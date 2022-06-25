package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {
    /*
     * @description:查询全部角色信息
     * @author: niuzp
     * @date: 2022/6/21 18:28
     **/
    List<Role> findAll();
     /*
      * @description:查询用户的角色列表
      * @author: niuzp
      * @date: 2022/6/21 18:27
      **/
    Map<String, List<Role>> findRolesByAdminId(Long id);

    void saveAdminRole(Long adminId, List<Long> roleIds);
}
