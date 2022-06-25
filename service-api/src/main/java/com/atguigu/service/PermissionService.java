package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    /**
     * 根据角色获取权限数据
     * @return
     */
     List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    /**
     * 保存角色权限信息
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 查询用户的所有权限
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 查询所有菜单
     * @return
     */
    List<Permission> findAllMenu();

    /**
     * 查询用户的操作权限code
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);
}
