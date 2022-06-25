package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;



    private static final String LIST_ACTION = "redirect:/community";
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";

    @RequestMapping
    public String  index(@RequestParam Map<String,Object> filters, Model model){
        //判断是否传染了pageNum和pageSize
        if (filters.get("pageNum")==null || "".equals(filters.get("pageNum"))){
            filters.put("pageNum",1);
        }
        if (filters.get("pageSize")==null || "".equals(filters.get("pageSize"))){
            filters.put("pageSize",10);
        }
        //调用业务层方法查询分页信息
        PageInfo<Community> page = communityService.findPage(filters);

        //分页信息放到请求域里
        model.addAttribute("page",page);
        //查询"beijing"区域list放在请求域
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);


        //页面回显时要areaId和plateId
        if (filters.get("areaId")==null || "".equals(filters.get("areaId"))){
            filters.put("areaId",0);
        }
        if (filters.get("plateId")==null || "".equals(filters.get("plateId"))){
            filters.put("plateId",0);
        }

        model.addAttribute("filters",filters);

          return PAGE_INDEX;
    }


    @RequestMapping("/create")
    public String create(Model model){
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList" ,areaList);
        return PAGE_CREATE;
    }
    @PostMapping("/save")
    public String save( Community community,Model model){
        //调用业务层处理
        communityService.insert(community);
         return successPage(model,"添加小区成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
       //查询小区信息
        Community community = communityService.getById(id);
        model.addAttribute("community",community);
        //查询"beijing"所有的区域
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        model.addAttribute("areaList",areaList);
      return PAGE_EDIT;
    }

    @RequestMapping("/update")
    public  String update(Community community,Model model){
        //调业务层处理
        communityService.update(community);
        return successPage(model,"更新小区成功");
    }

    @GetMapping("/delete/{id}")
    public  String delete(@PathVariable("id") Long id,Model model){
        //调取业务层处理
        communityService.delete(id);

        return LIST_ACTION;
    }
}
