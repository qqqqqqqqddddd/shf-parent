package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.github.pagehelper.util.StringUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
     @Reference
     private  PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查找用户
        Admin admin =adminService.getByUsername(username);
        if(null == admin) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        //获取用户的权限列表
        List<String> codePermissionList = permissionService.findCodePermissionListByAdminId(admin.getId());

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        for (String code : codePermissionList) {
            if (StringUtils.isEmpty(code)){
                continue;
            }
            grantedAuthorityList.add(new SimpleGrantedAuthority(code));
        }
        //查询出用户的所有权限交给spring-security
        return new User(username,admin.getPassword(),grantedAuthorityList);

    }
}
