package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static  final  String PAGE_INDEX ="admin/index";
    private  static  final  String PAGE_EDIT = "admin/edit";
    private final static String LIST_ACTION = "redirect:/admin";
    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    @RequestMapping
    public  String  Index(Model model, Map<String,Object> filters){
        //处理pageNum和pageSize为空的情况
        if (!filters.containsKey("pageNum")) {
            filters.put("pageNum",1);
        }
        if (!filters.containsKey("pageSize")) {
            filters.put("pageSize",10);
        }
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);
      return PAGE_INDEX;
    }

    @PostMapping("/save")
    public  String save(Admin admin, Model model){
        //设置密码
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        //调用业务层方法
        adminService.insert(admin);
        return  successPage(model,"新增用户成功");

    }
    @GetMapping("/edit/{id}")
    public  String edit(@PathVariable Long id, Model model){
        //调用业务层方法
        Admin admin = adminService.getById(id);
        model.addAttribute("admin",admin);
        return PAGE_EDIT ;
    }

    @PostMapping("/update")
    public  String update(Model model,Admin admin){
        //调用业务层方法
        adminService.update(admin);
        return successPage(model,"编辑用户成功") ;
    }
    @GetMapping("/delete/{id}")
    public  String delete(@PathVariable Long id, Model model){
        //调用业务层方法
        adminService.delete(id);
        return LIST_ACTION;
    }
    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id, Model model){
           model.addAttribute("id",id);

           return  PAGE_UPLOAD_SHOW;

    }

    @PostMapping("/upload/{id}")
    public String delete(
            @PathVariable("id") Long id,
            @RequestParam("file") MultipartFile multipartFile,
            Model model) throws IOException {
        //上传图片到七牛云
        //生成唯一的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        String uuidName = FileUtil.getUUIDName(originalFilename);
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),uuidName);
       //保存图片到数据库
        String headUrl = QiniuUtils.getUrl(uuidName);
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(headUrl);

        //更新admin
        adminService.update(admin);
        //返回成功通知
        return successPage(model,"上传头像成功");
    }
   @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //调用业务层查询用户已经有的和还没有的角色
      Map<String, List<Role>> roleListMap  =  roleService.findRolesByAdminId(id);
       //将查询到的数据存储到请求域里,key是assignRoleList和unAssignRoleList

       //将id存储到请求域:为了在分配角色页面拿到adminId
       model.addAttribute("adminId",id);
       model.addAllAttributes(roleListMap);
       
       return PAGE_ASSIGN_SHOW;
    }


    @PostMapping("/assignRole")
    public  String assignRole(@RequestParam("adminId") Long adminId,
                              @RequestParam("roleIds") List<Long> roleIds,
                              Model model){
        //调用业务层保存
        roleService.saveAdminRole(adminId,roleIds);
        return  successPage(model,"保存角色成功");
    }


}
