package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Reference
   private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    private static final String PAGE_CREATE = "role/create";
    private static  final  String PAGE_INDEX ="role/index";
    private final static String PAGE_EDIT = "role/edit";
    private final static String LIST_ACTION = "redirect:/role";
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow" ;

    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(Model model, @RequestParam Map filters) {
        if (!filters.containsKey("pageNum")){
            filters.put("pageNum",1);
        }
        if(!filters.containsKey("pageSize")){
            filters.put("pageSize",10);
        }
        roleService.findPage(filters);

       PageInfo<Role> pageInfo= roleService.findPage(filters);
        model.addAttribute("page", pageInfo);
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }
//只是return逻辑视图可以用view-controller代替
//    @GetMapping("/create")
//    public  String create(){
//     return "role/create";
//    }
    @PreAuthorize("hasAnyAuthority('role.create')")
    @GetMapping("/create")
    public  String create(){
        return  PAGE_CREATE;
    }

    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public  String save(Role role, Model model){
    //调用业务层方法
     roleService.insert(role);
     return  successPage(model,"新增角色成功");

    }
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(Model model,@PathVariable Long id){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role,Model model){
        roleService.update(role);
       return successPage(model,"更新角色成功");
    }
    @PreAuthorize("hasAnyAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public  String delete(@PathVariable Long id){
        roleService.delete(id);
        return LIST_ACTION;
    }
    @PreAuthorize("hasAnyAuthority('role.assign')")
    @GetMapping("/assignShow/{roleId}")
    public  String assignShow(@PathVariable("roleId") Long roleId ,Model model){
        List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        model.addAttribute("roleId",roleId);
       return  PAGE_ASSIGN_SHOW;
    }
    @PreAuthorize("hasAnyAuthority('role.assign')")
    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,
                                   @RequestParam("permissionIds") List<Long> permissionIds,
                                   Model model){
        permissionService.saveRolePermission(roleId,permissionIds);
        return successPage(model,"设置角色权限成功");

    }

}
