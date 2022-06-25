package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String SHOW_ACTION = "redirect:/house/";
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;
    @Reference
    private HouseService houseService;

    @RequestMapping("/create")
    public  String  create(Model model, HouseBroker houseBroker){
        model.addAttribute("houseBroker",houseBroker);
        saveAdminListToModel(model);
        return PAGE_CREATE;

    }

    public void saveAdminListToModel(Model model){
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
    }

    @PostMapping("/save")
    public  String save(HouseBroker houseBroker,Model model){
        //判断当前管理员是否已经是当前房源的经纪人
        HouseBroker dbHouseBroker = houseBrokerService.getByHouseIdAndBrokerId(houseBroker.getHouseId(), houseBroker.getBrokerId());
        if(dbHouseBroker != null){
            //说明当前重复添加了经纪人
            throw new RuntimeException("重复添加,失败");
        }
        //否则,可以添加
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBroker.setBrokerName(admin.getName());

        houseBrokerService.insert(houseBroker);
        return successPage(model,"新增房源经纪人成功");
    }

    @GetMapping("/edit/{id}")
    public String  edit(@PathVariable("id") Long id,Model model){
        HouseBroker houseBroker = houseBrokerService.getById(id);
        saveAdminListToModel(model);
        //回显当前经纪人的信息
        model.addAttribute("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public  String  update(HouseBroker houseBroker,Model model){
        //根据brokerId查询到修改后的管理员
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        //把他的属性修改到houseBroker表里
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(houseBroker);
      return successPage(model,"修改房源经纪人成功");
    }

    @GetMapping("/delete/{houseId}/{id}")
    public  String delete(@PathVariable("id") Long id ,@PathVariable("houseId") Long houseId){

        houseBrokerService.delete(id);
        return SHOW_ACTION+houseId;
    }


}
