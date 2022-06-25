package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.stream.Collectors;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    protected BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long id) {
        //查询出所有角色列表
        List<Role> allRoleList = roleMapper.findAll();
        //查询出已经分配的角色id列表
        List<Long> roleIdList = adminRoleMapper.findRoleIdByAdminId(id);
        List<Role> assignRoleList =  allRoleList.stream()
                    .filter(role -> roleIdList.contains(role.getId()))
                    .collect(Collectors.toList());
        //查询出未分配的角色列表,即从所有里面排除已分配
        List<Role> unAssignRoleList = allRoleList.stream()
                .filter(role -> !roleIdList.contains(role.getId()))
                .collect(Collectors.toList());
        Map<String, List<Role>> map= new HashMap<>();
        map.put("assignRoleList",assignRoleList);
        map.put("unAssignRoleList",unAssignRoleList);
        return map;
    }

    @Override
    public void saveAdminRole(Long adminId, List<Long> roleIds) {
       //1.查询当前用户的已分配的id集合
        List<Long> assignRoleIds = adminRoleMapper.findRoleIdByAdminId(adminId);
        //2.找出要移除的角色id
        List<Long> removeRoleIds = assignRoleIds.stream()
                .filter(roleId -> !roleIds.contains(roleId)).collect(Collectors.toList());

/*        List<Long> removeRoles = new ArrayList<>();
        for (Long roleId : assignRoleIds) {
            if (!roleIds.contains(roleId)){
                //移除
                removeRoleIds.add(roleId);
            }
        }*/
        //3.调用持久层方法移除用户和角色的绑定
        if (removeRoleIds != null && removeRoleIds.size() >0){
            adminRoleMapper.removeAdminRole(adminId,removeRoleIds);
        }
        //4.遍历新分配的每个角色的id
        for (Long roleId : roleIds){
            AdminRole adminRole =adminRoleMapper.findByAdminIdAndRoleId(adminId,roleId);
            if (adminRole == null){
                //说明之前没有角色
                adminRole=new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                adminRoleMapper.insert(adminRole);
            }else{
                //说明之前绑定过角色
                if (adminRole.getIsDeleted() == 1){
                    //说明已经被移除了
                    //现修改为0
                    adminRole.setIsDeleted(0);
                    adminRoleMapper.update(adminRole);
                }
            }

        }
    }
}
