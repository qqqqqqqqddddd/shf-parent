package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    /**
     * 查询当前管理员的角色
     * @param id
     * @return
     */
     List<Long> findRoleIdByAdminId(Long id);

    /**
     * 查询用户是否绑定过角色
     * @param adminId
     * @param roleId
     * @return
     */
    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId,@Param("roleId") Long roleId);

    /**
     * 移除用户角色
     * @param adminId
     * @param removeRoleIdList
     */
     void removeAdminRole(@Param("adminId") Long adminId,@Param("roleIds") List<Long> removeRoleIdList);
}
