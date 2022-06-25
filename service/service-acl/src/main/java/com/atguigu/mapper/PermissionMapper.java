package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 查询所有权限信息
     * @return
     */
    List<Permission> findAll();

    /**
     * 查询用户的菜单权限列表
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);

    /**
     * 根据id查询当前菜单是否有子菜单
      * @param id
     * @return
     */
    Integer findCountByParentId(Long id);

    /**
     * 查询所有权限操作
     * @return
     */
    List<String> findAllCodePermission();

    /**
     * 根据adminId查询用户的权限操作
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);
}
