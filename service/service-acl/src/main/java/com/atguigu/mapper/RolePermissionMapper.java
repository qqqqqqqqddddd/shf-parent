package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Long> findPermissionIdListByRoleId(Long roleId);

    /**
     * 删除角色的权限
     * @param roleId
     * @param removePermissionIdList
     */
    void removeRolePermission(@Param("roleId") Long roleId, @Param("removePermissionIdList") List<Long> removePermissionIdList);

    /**
     * 根据roleId和permissionId查询
      * @param roleId
     * @param permissionId
     * @return
     */
    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
