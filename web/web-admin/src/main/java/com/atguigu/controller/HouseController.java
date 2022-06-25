package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;

import com.atguigu.en.DictCode;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;

import com.sun.corba.se.pept.broker.Broker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{
    @Reference
      private HouseService houseService;
    @Reference
      private CommunityService communityService;
    @Reference
      private DictService dictService;
    @Reference
      private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;

    private static final String PAGE_SHOW = "house/show";
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";

      @RequestMapping
      public  String  index(@RequestParam Map<String, Object> filters, Model model){
          //处理pageNum和pageSize为空的情况
          if (!filters.containsKey("pageNum")) {
              filters.put("pageNum",1);
          }
          if (!filters.containsKey("pageSize")) {
              filters.put("pageSize",10);
          }
          //调用业务层处理
          PageInfo<House> pageInfo = houseService.findPage(filters);

          model.addAttribute("page",pageInfo);
          model.addAttribute("filters",filters);

          saveAllDictToRequestScope(model);
          return PAGE_INDEX;
      }

    /*
     * @description:查询所有所有小区以及字典里的各个列表
     * @author: niuzp
     * @date: 2022/6/16 11:10
     **/
    public  void saveAllDictToRequestScope(Model model){
        //2. 查询所有小区
        List<Community> communityList = communityService.findAll();
        //3. 查询各种初始化列表:户型列表、楼层列表、装修情况列表....
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(DictCode.HOUSETYPE.getMessage());
        List<Dict> floorList = dictService.findDictListByParentDictCode(DictCode.FLOOR.getMessage());
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(DictCode.BUILDSTRUCTURE.getMessage());
        List<Dict> directionList = dictService.findDictListByParentDictCode(DictCode.DIRECTION.getMessage());
        List<Dict> decorationList = dictService.findDictListByParentDictCode(DictCode.DECORATION.getMessage());
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(DictCode.HOUSEUSE.getMessage());
        //5. 将所有小区存储到请求域
        model.addAttribute("communityList",communityList);
        //6. 将各种列表存储到请求域
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);
      }

    @GetMapping("/create")
    public  String create(Model model){

    saveAllDictToRequestScope(model);

    return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Model model,House house){
        //未发布
        house.setStatus(HouseStatus.UNPUBLISHED.code);
        houseService.insert(house);
        return successPage(model,"添加房源信息成功");
    }

    @RequestMapping("/edit/{id}")
    public  String  edit(@PathVariable("id") Long id ,Model model){
        saveAllDictToRequestScope(model);
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Model model,House house){
        houseService.update(house);
        return successPage(model,"修改房源信息成功");
    }

    @RequestMapping("/delete/{id}")
    public  String delete(@PathVariable("id") Long id){
        houseService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        return LIST_ACTION;
    }

    @GetMapping("/{houseId}")
    public String show(@PathVariable("houseId") Long houseId ,Model model){
        //查询房源的各个详细信息
        House house = houseService.getById(houseId);
        Community community = communityService.getById(house.getCommunityId());
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, 1);
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(houseId, 2);
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(houseId);
        //将信息放在请求域里
        model.addAttribute("house",house);
        model.addAttribute("community",community);
        model.addAttribute("houseImage1List",houseImage1List);
        model.addAttribute("houseImage2List",houseImage2List);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;

    }


}
